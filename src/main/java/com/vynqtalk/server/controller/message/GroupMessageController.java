package com.vynqtalk.server.controller.message;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.dto.response.ApiResponse;
import com.vynqtalk.server.mapper.GroupMessageMapper;
import com.vynqtalk.server.model.messages.GroupMessage;
import com.vynqtalk.server.model.messages.Reaction;
import com.vynqtalk.server.service.message.GroupMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/group_messages")
@RestController
public class GroupMessageController {

    private static final Logger logger = LoggerFactory.getLogger(GroupMessageController.class);

    private final GroupMessageService groupMessageService;
    private final GroupMessageMapper groupMessageMapper;

    public GroupMessageController(GroupMessageService groupMessageService, GroupMessageMapper groupMessageMapper) {
        this.groupMessageService = groupMessageService;
        this.groupMessageMapper = groupMessageMapper;
    }

    // Get all messages in a conversation or group
    @GetMapping("/conv/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupMessageDTO>>> getMessagesByConversation(@PathVariable Long groupId) {
        List<GroupMessage> messages = groupMessageService.getAllGroupMessages(groupId);
        List<GroupMessageDTO> messageDTO = messages.stream()
                .map(groupMessageMapper::toDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true,messageDTO, messageDTO.isEmpty() ? "No messages found for this group" : "Messages retrieved successfully"));
    }

    // Get message by ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> getMessages(@PathVariable Long conversationId) {
        GroupMessage message = groupMessageService.getGroupMessageById(conversationId);
        return ResponseEntity.ok(new ApiResponse<>(true,groupMessageMapper.toDTO(message), "Message retrieved successfully"));
    }

    // Delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long messageId) {
        logger.info("Deleting group message with id: {}", messageId);
        groupMessageService.deleteGroupMessage(messageId);
        return ResponseEntity.ok(new ApiResponse<>(true,null, "Message deleted successfully"));
    }

    // Update (edit) a message
    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> updateMessage(@PathVariable Long messageId,
            @RequestBody GroupMessage updated) {
        GroupMessage result = groupMessageService.updateGroupMessage(messageId, updated);
        return ResponseEntity.ok(new ApiResponse<>(true,groupMessageMapper.toDTO(result), "Message updated successfully"));
    }

    @PutMapping("/react/{messageId}")
    public ResponseEntity<ApiResponse<GroupMessageDTO>> reactMessage(@PathVariable Long messageId,
            @RequestBody List<Reaction> reactions) {
        GroupMessage result = groupMessageService.reactToMessage(messageId, reactions);
        return ResponseEntity.ok(new ApiResponse<>(true,groupMessageMapper.toDTO(result), "Message reactions updated successfully"));
    }
}
