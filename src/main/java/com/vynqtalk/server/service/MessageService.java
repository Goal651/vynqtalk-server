package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
