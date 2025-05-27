package com.vynqtalk.server.service;

import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message getMessagesByConversationId(Long conversationId) {
        return messageRepository.findById(conversationId).get();
    }
    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Long messageId, Message updatedMessage) {
        Optional<Message> existing = messageRepository.findById(messageId);
        if (existing.isPresent()) {
            Message message = existing.get();
            message.setContent(updatedMessage.getContent());
            message.setEdited(true);
            return messageRepository.save(message);
        } else {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }
    }
}
