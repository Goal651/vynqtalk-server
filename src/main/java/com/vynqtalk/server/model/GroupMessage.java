package com.vynqtalk.server.model;

import java.time.Instant;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "group_messages")
public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "receiver_id")
    private Group group;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private boolean isEdited;

    @Column(nullable = false)
    private List<String> reactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_to_message_id")
    private GroupMessage replyToMessage;

}
