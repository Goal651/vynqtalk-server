package com.vynqtalk.server.controller;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.GroupMessageMapper;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.service.GroupMessageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/group_messages")
@RestController
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    @Autowired
    private GroupMessageMapper groupMessageMapper;

    // Get all messages in a conversation or group
    @GetMapping("/conv/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupMessageDTO>>> getMessagesByConversation(@PathVariable Long groupId) {
        if (groupId == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Group ID cannot be null", 400));
        }

        List<GroupMessage> messages = groupMessageService.getAllGroupMessages(groupId);

        if (messages.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(null, "No messages found for this group", 404));
        }

        List<GroupMessageDTO> messageDTO = messages.stream()
                .map(groupMessageMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(messageDTO, "Messages retrieved successfully", 200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> getMessages(@PathVariable Long conversationId) {
        GroupMessage messages = groupMessageService.getGroupMessageById(conversationId);
        return ResponseEntity.ok(new ApiResponse<>(groupMessageMapper.toDTO(messages), "Message retrieved successfully", 200));
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long messageId) {
        System.out.println("messageId" + messageId);
        groupMessageService.deleteGroupMessage(messageId);
        return ResponseEntity.ok(new ApiResponse<>(null, "Message deleted successfully", 200));
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> updateMessage(@PathVariable Long messageId,
            @RequestBody GroupMessage updated) {
        GroupMessage result = groupMessageService.updateGroupMessage(messageId, updated);
        return ResponseEntity.ok(new ApiResponse<>(groupMessageMapper.toDTO(result), "Message updated successfully", 200));
    }

    @PutMapping("/react/{messageId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> reactMessage(@PathVariable Long messageId,
            @RequestBody List<String> reactions) {
        GroupMessage result = groupMessageService.reactToMessage(messageId, reactions);
        return ResponseEntity.ok(new ApiResponse<>(groupMessageMapper.toDTO(result), "Message reactions updated successfully", 200));
    }
}
