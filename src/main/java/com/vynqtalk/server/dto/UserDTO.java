package com.vynqtalk.server.dto;

import java.time.Instant;

public class UserDTO {
    public Long id;
    public String name;
    public String email;
    public Boolean isAdmin;
    public String status;
    public String bio;
    public Instant lastActive;
    public Instant createdAt;
}