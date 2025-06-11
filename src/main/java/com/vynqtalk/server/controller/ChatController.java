package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.mapper.GroupMessageMapper;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.service.GroupMessageService;
import com.vynqtalk.server.service.MessageService;
import com.vynqtalk.server.websocket.payload.ChatGroupMessage;
import com.vynqtalk.server.websocket.payload.ChatGroupMessageReply;
import com.vynqtalk.server.websocket.payload.ChatMessage;
import com.vynqtalk.server.websocket.payload.ChatMessageReply;
import com.vynqtalk.server.websocket.payload.ReactMessage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {


    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupMessageService groupMessageService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private GroupMessageMapper groupMessageMapper;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public MessageDTO receiveMessage(@Payload ChatMessage message) {
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
        return messageMapper.toDTO(saved);
    }

    @MessageMapping("/chat.sendMessageReply")
    @SendTo("/topic/messages")
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
        savedMessage = messageService.saveMessage(savedMessage);
        return messageMapper.toDTO(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReaction")
    @SendTo("/topic/reactions")
    public MessageDTO reactToMessage(@Payload ReactMessage message) {
        System.out.println("Received message: " + message);
        Optional<Message> exist = messageService.getMessageById(message.getMessageId());
        if (!exist.isPresent()) {
            return null;
        }
        Message savedMessage = exist.get();
        savedMessage.setReactions(message.getReactions());
        messageService.saveMessage(savedMessage);
        return messageMapper.toDTO(savedMessage);
    }

    // Group socket controller

    @MessageMapping("/group.sendMessage")
    @SendTo("/topic/groupMessages")
    public GroupMessageDTO receiveGroupMessage(@Payload ChatGroupMessage message) {
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
        return groupMessageMapper.toDTO(saved);
    }

    @MessageMapping("/group.sendMessageReply")
    public GroupMessageDTO replyGroupMessage(@Payload ChatGroupMessageReply message) {
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
        savedMessage = groupMessageService.saveGroupMessage(savedMessage);
        return groupMessageMapper.toDTO(savedMessage);
    }

    @MessageMapping("/group.sendMessageReaction")
    public String reactToGroupMessage(@Payload ReactMessage message) {
        System.out.println("Received message: " + message);
        GroupMessage groupMessage = groupMessageService.getGroupMessageById(message.getMessageId());
        if (groupMessage != null) {
            groupMessage.setReactions(message.getReactions());
            groupMessageService.saveGroupMessage(groupMessage); 
        }
        return "Successfully updated";
    }

}
