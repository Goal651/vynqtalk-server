package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.MessageDTO;
import com.vynqtalk.server.dto.UserDTO;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.sockets.ChatGroupMessage;
import com.vynqtalk.server.model.sockets.ChatGroupMessageReply;
import com.vynqtalk.server.model.sockets.ChatMessage;
import com.vynqtalk.server.model.sockets.ChatMessageReply;
import com.vynqtalk.server.model.sockets.ReactMessage;
import com.vynqtalk.server.service.GroupMessageService;
import com.vynqtalk.server.service.MessageService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ChatController {
    @Autowired

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupMessageService groupMessageService;

    // Example: Add to your ChatController or a Mapper class

    private UserDTO toUserDTO(User user) {
        if (user == null)
            return null;
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.isAdmin = user.getIsAdmin();
        return dto;
    }

    private MessageDTO toMessageDTO(Message message) {
        if (message == null)
            return null;
        MessageDTO dto = new MessageDTO();
        dto.id = message.getId();
        dto.content = message.getContent();
        dto.type = message.getType();
        dto.sender = toUserDTO(message.getSender());
        dto.receiver = toUserDTO(message.getReceiver());
        dto.timestamp = message.getTimestamp();
        dto.edited = message.isEdited();
        dto.reactions = message.getReactions();
        // Only map one level of replyToMessage to avoid deep recursion
        dto.replyToMessage = message.getReplyToMessage() != null ? toMessageDTOShallow(message.getReplyToMessage())
                : null;
        return dto;
    }

    private MessageDTO toMessageDTOShallow(Message message) {
        if (message == null)
            return null;
        MessageDTO dto = new MessageDTO();
        dto.id = message.getId();
        dto.content = message.getContent();
        dto.type = message.getType();
        dto.sender = toUserDTO(message.getSender());
        dto.receiver = toUserDTO(message.getReceiver());
        dto.timestamp = message.getTimestamp();
        dto.edited = message.isEdited();
        dto.reactions = message.getReactions();
        // Do not recurse further!
        return dto;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public Message receiveMessage(@Payload ChatMessage message) {
        logger.info("Received private message: {}", message);
        Message savedMessage = new Message();
        savedMessage.setSender(message.getSender());
        savedMessage.setReceiver(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());

        Message saved = messageService.saveMessage(savedMessage);
        logger.debug("Saved message to database: {}", saved);
        return saved;
    }

    @MessageMapping("/chat.sendMessageReply")
    @SendTo("/topic/messages")
    @Transactional
    public MessageDTO replyMessage(@Payload ChatMessageReply message) {
        Message savedMessage = new Message();
        savedMessage.setSender(message.getSender());
        savedMessage.setReceiver(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyToMessage(message.getReplyToMessage());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        Message saved = messageService.saveMessage(savedMessage);
        // Initialize lazy-loaded entities
        Hibernate.initialize(saved.getSender());
        Hibernate.initialize(saved.getReceiver());
        if (saved.getReplyToMessage() != null) {
            Hibernate.initialize(saved.getReplyToMessage());
            Hibernate.initialize(saved.getReplyToMessage().getSender());
        }
        return this.toMessageDTO(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReaction")
    @SendTo("/topic/reactions")
    @Transactional
    public Message reactToMessage(@Payload ReactMessage message) {
        System.out.println("Received message: " + message);
        Optional<Message> exist = messageService.getMessageById(message.getMessageId());
        if (!exist.isPresent()) {
            return null;
        }
        Message savedMessage = exist.get();

        // Initialize lazy-loaded entities
        Hibernate.initialize(savedMessage.getSender());
        Hibernate.initialize(savedMessage.getReceiver());
        if (savedMessage.getReplyToMessage() != null) {
            Hibernate.initialize(savedMessage.getReplyToMessage());
            Hibernate.initialize(savedMessage.getReplyToMessage().getSender());
        }
        savedMessage.setReactions(message.getReactions());
        messageService.saveMessage(savedMessage);

        return savedMessage;
    }

    // Group socket controller

    @MessageMapping("/group.sendMessage")
    @SendTo("/topic/groupMessages")
    public GroupMessage receiveGroupMessage(@Payload ChatGroupMessage message) {
        System.out.println("Received message: " + message);
        GroupMessage savedMessage = new GroupMessage();
        savedMessage.setSender(message.getSender());
        savedMessage.setGroup(message.getGroup());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        GroupMessage saved = groupMessageService.saveGroupMessage(savedMessage);
        return saved;
    }

    @MessageMapping("/group.sendMessageReply")
    public GroupMessage replyGroupMessage(@Payload ChatGroupMessageReply message) {
        System.out.println("Received message: " + message);
        GroupMessage savedMessage = new GroupMessage();
        savedMessage.setSender(message.getSender());
        savedMessage.setGroup(message.getGroup());
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyToMessage(message.getReplyToMessage());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        return groupMessageService.saveGroupMessage(savedMessage);
    }

    @MessageMapping("/group.sendMessageReaction")
    public String reactToGroupMessage(@Payload ReactMessage message) {
        System.out.println("Received message: " + message);
        Optional<GroupMessage> exist = groupMessageService.getGroupMessageById(message.getMessageId());
        if (exist.isPresent()) {
            GroupMessage savedMessage = exist.get();
            savedMessage.setReactions(message.getReactions());
            groupMessageService.saveGroupMessage(savedMessage); // <-- persist the update
        }
        return "Successfully updated";
    }

}
