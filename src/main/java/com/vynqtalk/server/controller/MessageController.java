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
        return ResponseEntity.ok(new MessagesResponse(messages, "Messages retrieved successfully", 200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<Message> getMessages(@PathVariable Long conversationId) {
        Message messages = messageService.getMessageById(conversationId).get();
        return ResponseEntity.ok(messages);
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        System.out.println("messageId" + messageId);
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId, @RequestBody Message updated) {
        Message result = messageService.updateMessage(messageId, updated);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/react/{messageId}")
    public ResponseEntity<Message> reactMessage(@PathVariable Long messageId, @RequestBody List<String> reactions) {
        Message result = messageService.reactToMessage(messageId, reactions);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/reply/{messageId}")
    public ResponseEntity<Message> replyToMessage(@PathVariable Long messageId, @RequestBody Message reply) {
        Message result = messageService.replyMessage(messageId, reply);
        return ResponseEntity.ok(result);
    }
}
