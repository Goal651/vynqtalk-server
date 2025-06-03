package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.MessageDTO;
import com.vynqtalk.server.dto.UserDTO;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.User;
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

    // Example: Add to your ChatController or a Mapper class

private UserDTO toUserDTO(User user) {
    if (user == null) return null;
    UserDTO dto = new UserDTO();
    dto.id = user.getId();
    dto.name = user.getName();
    dto.email = user.getEmail();
    dto.isAdmin = user.getIsAdmin();
    return dto;
}

private MessageDTO toMessageDTO(Message message) {
    if (message == null) return null;
    MessageDTO dto = new MessageDTO();
    dto.id = message.getId();
    dto.content = message.getContent();
    dto.type = message.getType();
    dto.sender = toUserDTO(message.getSender());
    dto.receiver = toUserDTO(message.getReceiver());
    dto.timestamp = message.getTimestamp();
    dto.edited = message.isEdited();
    dto.reactions = message.getReactions();
    // Only map one level of replyToMessage to avoid deep recursion
    dto.replyToMessage = message.getReplyToMessage() != null ? toMessageDTOShallow(message.getReplyToMessage()) : null;
    return dto;
}

private MessageDTO toMessageDTOShallow(Message message) {
    if (message == null) return null;
    MessageDTO dto = new MessageDTO();
    dto.id = message.getId();
    dto.content = message.getContent();
    dto.type = message.getType();
    dto.sender = toUserDTO(message.getSender());
    dto.receiver = toUserDTO(message.getReceiver());
    dto.timestamp = message.getTimestamp();
    dto.edited = message.isEdited();
    dto.reactions = message.getReactions();
    // Do not recurse further!
    return dto;
}

    // Get all messages in a conversation or group
    @GetMapping("/conv/{senderId}/{receiverId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessagesByConversation(@PathVariable Long senderId,
            @PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        List<MessageDTO> result=  messages.stream().map(this::toMessageDTO).toList();
        return ResponseEntity.ok(new ApiResponse<>(result, "Messages retrieved successfully", 200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<Message>> getMessages(@PathVariable Long conversationId) {
        Message messages = messageService.getMessageById(conversationId).get();
        return ResponseEntity.ok(new ApiResponse<>(messages, "Message retrieved successfully", 200));
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
    public ResponseEntity<ApiResponse<Message>> updateMessage(@PathVariable Long messageId,
            @RequestBody Message updated) {
        Message result = messageService.updateMessage(messageId, updated);
        return ResponseEntity.ok(new ApiResponse<>(result, "Message updated successfully", 200));
    }

}
