package com.vynqtalk.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vynqtalk.server.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
