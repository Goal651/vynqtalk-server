package com.vynqtalk.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String avatar;
    private String description;
    private String createdBy;
    private String createdAt;
    private Boolean isPrivate;
    private String[] members;
}
