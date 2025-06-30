package com.vynqtalk.server.service;

import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.Reaction;
import com.vynqtalk.server.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vynqtalk.server.error.MessageNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Saves a message and creates a notification.
     */
    @Transactional
    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }

    /**
     * Gets a message by ID.
     */
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    /**
     * Gets messages between two users.
     */
    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepository.findChatBetweenUsers(senderId, receiverId);
    }

    /**
     * Deletes a message by ID.
     * @throws MessageNotFoundException if not found
     */
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + messageId));
        // createMessageDeletedNotification(message);
        messageRepository.deleteById(messageId);
    }

    /**
     * Updates a message by ID.
     * @throws MessageNotFoundException if not found
     */
    @Transactional
    public Message updateMessage(Long messageId, Message updatedMessage) {
        messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + messageId));
        Message savedMessage = messageRepository.save(updatedMessage);
        // createMessageEditedNotification(savedMessage);
        return savedMessage;
    }

    /**
     * Reacts to a message by ID.
     * @throws MessageNotFoundException if not found
     */
    @Transactional
    public Message reactToMessage(Long messageId, List<Reaction> reactions) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + messageId));
        message.setReactions(reactions);
        Message savedMessage = messageRepository.save(message);
        // createMessageReactionNotification(savedMessage);
        return savedMessage;
    }

    /**
     * Replies to a message by ID.
     * @throws MessageNotFoundException if not found
     */
    @Transactional
    public Message replyMessage(Long messageId, Message reply) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + messageId));
        reply.setReplyTo(message);
        reply.setTimestamp(Instant.now());
        Message savedReply = messageRepository.save(reply);
        // createMessageReplyNotification(savedReply, message);
        return savedReply;
    }

}
