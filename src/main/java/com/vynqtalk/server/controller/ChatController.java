package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.ChatMessage;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.ReactMessage;
import com.vynqtalk.server.service.MessageService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage") // front-end will send to /app/chat.sendMessage
    @SendTo("/topic/public") // sent back to all clients subscribed to /topic/public
    public ChatMessage receiveMessage(@Payload ChatMessage message) {
        Message savedMessage = new Message();
        savedMessage.setSenderId(message.getSender());
        savedMessage.setReceiverId(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType("text");
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(message.getReactions());
        messageService.saveMessage(savedMessage);
        return message;
    }

    @MessageMapping("/chat.chat.reactToMessage")
    @SendTo("/topic/public")
    public String reactToMessage(@Payload ReactMessage message){
        Optional<Message> exist=messageService.getMessageById(message.getId());
        if(exist.isPresent()){
            Message savedMessage=exist.get();
            List<String> reactions=savedMessage.getReactions();
            reactions.add(message.getReaction());
            savedMessage.setReactions(reactions);
        }
        return "Successfully updated";
    }
}
