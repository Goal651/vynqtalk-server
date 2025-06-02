package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.response.ApiResponse;
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

    // Get all messages in a conversation or group
    @GetMapping("/conv/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupMessage>>> getMessagesByConversation(@PathVariable Long groupId) {
        List<GroupMessage> messages = groupMessageService.getAllGroupMessages(groupId);
        return ResponseEntity.ok(new ApiResponse<>(messages, "Messages retrieved successfully", 200));
    }

    // Get messages by conversation ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<GroupMessage>> getMessages(@PathVariable Long conversationId) {
        GroupMessage messages = groupMessageService.getGroupMessageById(conversationId).get();
        return ResponseEntity.ok(new ApiResponse<>(messages, "Message retrieved successfully", 200));
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
    public ResponseEntity<ApiResponse<GroupMessage>> updateMessage(@PathVariable Long messageId,
            @RequestBody GroupMessage updated) {
        GroupMessage result = groupMessageService.updateGroupMessage(messageId, updated);
        return ResponseEntity.ok(new ApiResponse<>(result, "Message updated successfully", 200));
    }

    @PutMapping("/react/{messageId}")
    public ResponseEntity<ApiResponse<GroupMessage>> reactMessage(@PathVariable Long messageId,
            @RequestBody List<String> reactions) {
        GroupMessage result = groupMessageService.reactToMessage(messageId, reactions);
        return ResponseEntity.ok(new ApiResponse<>(result, "Message reactions updated successfully", 200));
    }
}
