package com.vynqtalk.server.dto.notifications;

import lombok.Data;

@Data
public class PushSubscriptionDTO {
    private String endpoint;
    private Keys keys;

    @Data
    public static class Keys {
        private String p256dh;
        private String auth;
    }
}