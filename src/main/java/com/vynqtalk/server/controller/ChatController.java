package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.Message;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupMessageService groupMessageService;

    @MessageMapping("/chat.sendMessage")
    public Message receiveMessage(@Payload ChatMessage message) {
        System.out.println("Received message: " + message);
        Message savedMessage = new Message();
        savedMessage.setSender(message.getSender());
        savedMessage.setReceiver(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        Message saved = messageService.saveMessage(savedMessage);
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getEmail(),
                "/queue/private",
                saved);
        return saved;
    }

    @MessageMapping("/chat.sendMessageReply") // front-end will send to /app/chat.sendMessage
    public Message replyMessage(@Payload ChatMessageReply message) {
        System.out.println("Received message: " + message);
        Message savedMessage = new Message();
        savedMessage.setSender(message.getSender());
        savedMessage.setReceiver(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyToMessage(message.getReplyToMessage());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        return messageService.saveMessage(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReaction")
    public String reactToMessage(@Payload ReactMessage message) {
        System.out.println("Received message: " + message);
        Optional<Message> exist = messageService.getMessageById(message.getMessageId());
        if (exist.isPresent()) {
            Message savedMessage = exist.get();
            savedMessage.setReactions(message.getReactions());
            messageService.saveMessage(savedMessage); // <-- persist the update
        }
        return "Successfully updated";
    }

    // Group socket controller

    @MessageMapping("/chat.sendGroupMessage")
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
        messagingTemplate.convertAndSend(
                "/topic/group/" + message.getGroup().getId(),
                saved);
        return saved;
    }

    @MessageMapping("/chat.sendGroupMessageReply") // front-end will send to /app/chat.sendMessage
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

    @MessageMapping("/chat.sendGroupMessageReaction")
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
