package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.MessageDTO;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.response.ApiResponse;
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

    @Autowired
    private MessageMapper messageMapper;

    // Get all messages in a conversation or group
    @GetMapping("/conv/{senderId}/{receiverId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessagesByConversation(@PathVariable Long senderId,
            @PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        List<MessageDTO> result = messages.stream().map(messageMapper::toDTO).toList();
        return ResponseEntity.ok(new ApiResponse<>(result, "Messages retrieved successfully", 200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<MessageDTO>> getMessages(@PathVariable Long conversationId) {
        Message messages = messageService.getMessageById(conversationId).get();
        return ResponseEntity
                .ok(new ApiResponse<>(messageMapper.toDTO(messages), "Message retrieved successfully", 200));
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long messageId) {
        System.out.println("messageId" + messageId);
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok(new ApiResponse<>(null, "Message deleted successfully", 200));
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<MessageDTO>> updateMessage(@PathVariable Long messageId,
            @RequestBody Message updated) {
        Message result = messageService.updateMessage(messageId, updated);
        return ResponseEntity.ok(new ApiResponse<>(messageMapper.toDTO(result), "Message updated successfully", 200));
    }

}
