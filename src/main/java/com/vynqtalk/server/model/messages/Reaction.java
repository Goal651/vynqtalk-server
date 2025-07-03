package com.vynqtalk.server.model.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reaction {
    private String emoji;
    private Long userId;
}
