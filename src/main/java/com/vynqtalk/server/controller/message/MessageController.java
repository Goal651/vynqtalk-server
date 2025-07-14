package com.vynqtalk.server.controller.message;

import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.service.message.MessageService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/messages")
@RestController
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    // Get all messages in a conversation or group
    @GetMapping("/all/{senderId}/{receiverId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessagesByConversation(@PathVariable Long senderId,
            @PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        List<MessageDTO> result = messages.stream().map(messageMapper::toDTO).toList();
        return ResponseEntity.ok(new ApiResponse<>(true,result, "Messages retrieved successfully"));
    }

    // Get message by ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<MessageDTO>> getMessages(@PathVariable Long conversationId) {
        Message message = messageService.getMessageById(conversationId);
        return ResponseEntity
                .ok(new ApiResponse<>(true,messageMapper.toDTO(message), "Message retrieved successfully"));
    }

}
