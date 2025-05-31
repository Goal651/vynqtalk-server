package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.ChatMessage;
import com.vynqtalk.server.model.ChatMessageReply;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.ReactMessage;
import com.vynqtalk.server.service.MessageService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message receiveMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        System.out.println("Message from authenticated user: {}" + userId);

        Message savedMessage = new Message();
        savedMessage.setSenderId(message.getSenderId());
        savedMessage.setReceiverId(message.getReceiverId());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        return messageService.saveMessage(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReply") // front-end will send to /app/chat.sendMessage
    @SendTo("/topic/public") // sent back to all clients subscribed to /topic/public
    public Message receiveMessage(@Payload ChatMessageReply message) {
        System.out.println("Received message: " + message);
        Message savedMessage = new Message();
        savedMessage.setSenderId(message.getSenderId());
        savedMessage.setReceiverId(message.getReceiverId());
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyToMessageId(message.getReplyToMessage());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        return messageService.saveMessage(savedMessage);
    }

    @MessageMapping("/chat.sendMessageReaction")
    @SendTo("/topic/public")
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
}
