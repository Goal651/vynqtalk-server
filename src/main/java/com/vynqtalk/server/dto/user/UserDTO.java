package com.vynqtalk.server.dto.user;

import java.time.Instant;

public class UserDTO {
    public Long id;
    public String name;
    public String avatar;
    public String email;
    public Boolean isAdmin;
    public String status;
    public String bio;
    public Instant lastActive;
    public Instant createdAt;
}