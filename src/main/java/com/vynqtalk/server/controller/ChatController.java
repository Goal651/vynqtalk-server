package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.dto.websocket.ChatGroupMessage;
import com.vynqtalk.server.dto.websocket.ChatGroupMessageReply;
import com.vynqtalk.server.dto.websocket.ChatMessage;
import com.vynqtalk.server.dto.websocket.ChatMessageReply;
import com.vynqtalk.server.dto.websocket.ReactMessage;
import com.vynqtalk.server.mapper.GroupMessageMapper;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.service.GroupMessageService;
import com.vynqtalk.server.service.MessageService;
import com.vynqtalk.server.error.MessageNotFoundException;
import com.vynqtalk.server.error.GroupMessageNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final MessageService messageService;
    private final GroupMessageService groupMessageService;
    private final MessageMapper messageMapper;
    private final GroupMessageMapper groupMessageMapper;

    public ChatController(MessageService messageService, GroupMessageService groupMessageService, MessageMapper messageMapper, GroupMessageMapper groupMessageMapper) {
        this.messageService = messageService;
        this.groupMessageService = groupMessageService;
        this.messageMapper = messageMapper;
        this.groupMessageMapper = groupMessageMapper;
    }

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
        logger.info("Received reply message: {}", message);
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
        logger.info("Received message reaction: {}", message);
        Message exist = messageService.getMessageById(message.getMessageId())
            .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + message.getMessageId()));
        exist.setReactions(message.getReactions());
        Message savedMessage = messageService.saveMessage(exist);
        return messageMapper.toDTO(savedMessage);
    }

    // Group socket controller

    @MessageMapping("/group.sendMessage")
    @SendTo("/topic/groupMessages")
    public GroupMessageDTO receiveGroupMessage(@Payload ChatGroupMessage message) {
        logger.info("Received group message: {}", message);
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
        logger.info("Received group reply message: {}", message);
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
    @SendTo("/topic/groupReactions")
    public GroupMessageDTO reactToGroupMessage(@Payload ReactMessage message) {
        logger.info("Received group message reaction: {}", message);
        GroupMessage groupMessage = groupMessageService.getGroupMessageById(message.getMessageId());
        if (groupMessage == null) {
            throw new GroupMessageNotFoundException("Group message not found with id: " + message.getMessageId());
        }
        groupMessage.setReactions(message.getReactions());
        GroupMessage saved = groupMessageService.saveGroupMessage(groupMessage);
        return groupMessageMapper.toDTO(saved);
    }
}
