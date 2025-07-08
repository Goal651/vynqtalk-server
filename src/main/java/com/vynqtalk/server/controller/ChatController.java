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
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.messages.GroupMessage;
import com.vynqtalk.server.service.GroupMessageService;
import com.vynqtalk.server.service.GroupService;
import com.vynqtalk.server.service.MessageService;
import com.vynqtalk.server.service.UserService;

import java.time.Instant;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final GroupMessageService groupMessageService;
    private final MessageMapper messageMapper;
    private final GroupMessageMapper groupMessageMapper;
    private final UserService userService;
    private final GroupService groupService;

    public ChatController(MessageService messageService, GroupMessageService groupMessageService,
            MessageMapper messageMapper, GroupMessageMapper groupMessageMapper, UserService userService,
            GroupService groupService) {
        this.messageService = messageService;
        this.groupMessageService = groupMessageService;
        this.messageMapper = messageMapper;
        this.groupMessageMapper = groupMessageMapper;
        this.userService = userService;
        this.groupService = groupService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public MessageDTO receiveMessage(@Payload ChatMessage message) {
        Message savedMessage = new Message();
        User sender = userService.getUserById(message.getSenderId());
        User receiver = userService.getUserById(message.getReceiverId());

        savedMessage.setSender(sender);
        savedMessage.setReceiver(receiver);
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());

        Message saved = messageService.saveMessage(savedMessage);
        return messageMapper.toDTO(saved);
    }

    @MessageMapping("/chat.sendMessageReply")
    @SendTo("/topic/messages")
    public MessageDTO replyMessage(@Payload ChatMessageReply message) {
        Message savedMessage = new Message();
        User sender = userService.getUserById(message.getSenderId());
        User receiver = userService.getUserById(message.getSenderId());
        Message replyTo = messageService.getMessageById(message.getReplyToId());
        savedMessage.setSender(sender);
        savedMessage.setReceiver(receiver);
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyTo(replyTo);
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        savedMessage = messageService.saveMessage(savedMessage);
        return messageMapper.toDTO(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReaction")
    @SendTo("/topic/reactions")
    public MessageDTO reactToMessage(@Payload ReactMessage message) {
        Message exist = messageService.getMessageById(message.getMessageId());
        exist.setReactions(message.getReactions());
        Message savedMessage = messageService.saveMessage(exist);
        return messageMapper.toDTO(savedMessage);
    }

    // Group socket controller

    @MessageMapping("/group.sendMessage")
    @SendTo("/topic/groupMessages")
    public GroupMessageDTO receiveGroupMessage(@Payload ChatGroupMessage message) {
        User sender = userService.getUserById(message.getSenderId());
        Group group = groupService.findById(message.getSenderId());
        GroupMessage savedMessage = new GroupMessage();
        savedMessage.setSender(sender);
        savedMessage.setGroup(group);
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        GroupMessage saved = groupMessageService.saveGroupMessage(savedMessage);
        return groupMessageMapper.toDTO(saved);
    }

    @MessageMapping("/group.sendMessageReply")
    public GroupMessageDTO replyGroupMessage(@Payload ChatGroupMessageReply message) {
        GroupMessage savedMessage = new GroupMessage();
        User sender = userService.getUserById(message.getSenderId());
        Group group = groupService.findById(message.getSenderId());
        GroupMessage replyTo = groupMessageService.getGroupMessageById(message.getReplyToId());
        savedMessage.setSender(sender);
        savedMessage.setGroup(group);
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyTo(replyTo);
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        savedMessage = groupMessageService.saveGroupMessage(savedMessage);
        return groupMessageMapper.toDTO(savedMessage);
    }

    @MessageMapping("/group.sendMessageReaction")
    @SendTo("/topic/groupReactions")
    public GroupMessageDTO reactToGroupMessage(@Payload ReactMessage message) {
        GroupMessage groupMessage = groupMessageService.getGroupMessageById(message.getMessageId());
        groupMessage.setReactions(message.getReactions());
        GroupMessage saved = groupMessageService.saveGroupMessage(groupMessage);
        return groupMessageMapper.toDTO(saved);
    }
}
