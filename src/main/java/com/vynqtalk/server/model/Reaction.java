package com.vynqtalk.server.model;

import jakarta.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Column(nullable = false)
    private String emoji;

    @Column(nullable = false)
    private User user;

}

