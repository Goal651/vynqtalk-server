package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.response.MessagesResponse;
import com.vynqtalk.server.service.MessageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/messages")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Get all messages in a conversation or group
    @GetMapping("/conv/{senderId}/{receiverId}")
    public ResponseEntity<MessagesResponse> getMessagesByConversation(@PathVariable Long senderId,
            @PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        return ResponseEntity.ok(new MessagesResponse(messages,"Messages retrieved successfully",200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<Message> getMessages(@PathVariable Long conversationId) {
        Message messages = messageService.getMessageById(conversationId).get();
        return ResponseEntity.ok(messages);
    }

    // Send a new message
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        System.out.println("messageId"+messageId);
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId, @RequestBody Message updated) {
        Message result = messageService.updateMessage(messageId, updated);
        return ResponseEntity.ok(result);
    }
}
