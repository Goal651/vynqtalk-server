package com.vynqtalk.server.service.message;

import com.vynqtalk.server.exceptions.MessageNotFoundException;
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.model.messages.Reaction;
import com.vynqtalk.server.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Saves a message and creates a notification.
     */

    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }

    /**
     * Gets a message by ID.
     */
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElseThrow(
            ()-> new MessageNotFoundException()
        );
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
    
    public void deleteMessage(Long messageId) {
         messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException());
        // createMessageDeletedNotification(message);
        messageRepository.deleteById(messageId);
    }

    /**
     * Updates a message by ID.
     * @throws MessageNotFoundException if not found
     */
    
    public Message updateMessage(Long messageId, Message updatedMessage) {
        messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException());
        Message savedMessage = messageRepository.save(updatedMessage);
        // createMessageEditedNotification(savedMessage);
        return savedMessage;
    }

    /**
     * Reacts to a message by ID.
     * @throws MessageNotFoundException if not found
     */
    
    public Message reactToMessage(Long messageId, List<Reaction> reactions) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException());
        message.setReactions(reactions);
        Message savedMessage = messageRepository.save(message);
        // createMessageReactionNotification(savedMessage);
        return savedMessage;
    }

    /**
     * Replies to a message by ID.
     * @throws MessageNotFoundException if not found
     */
    
    public Message replyMessage(Long messageId, Message reply) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException());
        reply.setReplyTo(message);
        reply.setTimestamp(Instant.now());
        Message savedReply = messageRepository.save(reply);
        // createMessageReplyNotification(savedReply, message);
        return savedReply;
    }
 
    /**
     * Gets the latest message sent or received by a user.
     */
    public Message getLatestMessageByUserId(Long userId,Long currentUser) {
        List<Message> messages = messageRepository.findLatestMessageByUserId(userId,currentUser);
        return messages.isEmpty() ? null : messages.get(0);
    }

    /**
     * Gets unread messages for a user (where receiver is the user and isRead is false).
     */
    public List<Message> getUnreadMessagesByUserId(Long userId) {
        return messageRepository.findUnreadMessagesByUserId(userId);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteAllMessagesByUserId(Long userId) {
        java.util.List<Message> sent = messageRepository.findAllBySenderId(userId);
        java.util.List<Message> received = messageRepository.findAllByReceiverId(userId);
        sent.forEach(m -> messageRepository.deleteById(m.getId()));
        received.forEach(m -> messageRepository.deleteById(m.getId()));
    }
}
