package com.vynqtalk.server.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ReplyTo {
    private Long messageId;

    private String userId;

    private String userName;

    @Column(columnDefinition = "TEXT",name = "reply_content")
    private String content;

}
