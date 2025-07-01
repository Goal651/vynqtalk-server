package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.service.MessageService;
import com.vynqtalk.server.error.MessageNotFoundException;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/messages")
@RestController
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    // Get all messages in a conversation or group
    @GetMapping("/conv/{senderId}/{receiverId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessagesByConversation(@PathVariable Long senderId,
            @PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        List<MessageDTO> result = messages.stream().map(messageMapper::toDTO).toList();
        return ResponseEntity.ok(new ApiResponse<>(result, "Messages retrieved successfully", 200));
    }

    // Get message by ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<MessageDTO>> getMessages(@PathVariable Long conversationId) {
        Message message = messageService.getMessageById(conversationId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + conversationId));
        return ResponseEntity
                .ok(new ApiResponse<>(messageMapper.toDTO(message), "Message retrieved successfully", 200));
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long messageId) {
        messageService.getMessageById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok(new ApiResponse<>(null, "Message deleted successfully", 200));
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<MessageDTO>> updateMessage(@PathVariable Long messageId,
            @RequestBody Message updated) {
        Message message = messageService.getMessageById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
        message.setContent(updated.getContent());
        message.setEdited(true);
        Message result = messageService.updateMessage(messageId, message);
        return ResponseEntity.ok(new ApiResponse<>(messageMapper.toDTO(result), "Message updated successfully", 200));
    }

}
