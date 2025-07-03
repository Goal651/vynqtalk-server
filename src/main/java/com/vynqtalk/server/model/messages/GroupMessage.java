package com.vynqtalk.server.model.messages;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonType;
import com.vynqtalk.server.model.enums.MessageType;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_messages")
public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, name = "receiver_id")
    private Group group;

    @Column(nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private MessageType type;

    @Column(nullable = false)
    private boolean isEdited;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<Reaction> reactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_to_id")
    private GroupMessage replyTo;

}
