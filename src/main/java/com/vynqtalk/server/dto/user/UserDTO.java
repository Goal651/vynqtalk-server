package com.vynqtalk.server.dto.user;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.model.enums.UserRole;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String avatar;
    private String password ;
    private String email;
    private UserRole userRole;
    private String status;
    private String bio;
    private Instant lastActive;
    private Instant createdAt;
    private MessageDTO latestMessage;
    private List<MessageDTO> unreadMessages;
    private Boolean online;
}