package com.vynqtalk.server.service;

import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.Notification;
import com.vynqtalk.server.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationService notificationService;

    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        createMessageNotification(savedMessage);
        return savedMessage;
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepository.findChatBetweenUsers(senderId, receiverId);
    }

    public void deleteMessage(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            // createMessageDeletedNotification(message);
            messageRepository.deleteById(messageId);
        }
    }

    public Message updateMessage(Long messageId, Message updatedMessage) {
        Optional<Message> existing = messageRepository.findById(messageId);
        if (existing.isPresent()) {
            Message savedMessage = messageRepository.save(updatedMessage);
            // createMessageEditedNotification(savedMessage);
            return savedMessage;
        } else {
            return null;
        }
    }

    public Message reactToMessage(Long messageId, List<String> reactions) {
        Optional<Message> existing = messageRepository.findById(messageId);
        if (existing.isPresent()) {
            Message message = existing.get();
            message.setReactions(reactions);
            Message savedMessage = messageRepository.save(message);
            // createMessageReactionNotification(savedMessage);
            return savedMessage;
        } else {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }
    }

    public Message replyMessage(Long messageId, Message reply) {
        Optional<Message> existing = messageRepository.findById(messageId);
        if (existing.isPresent()) {
            Message message = existing.get();
            reply.setReplyToMessage(message);
            reply.setTimestamp(Instant.now());
            Message savedReply = messageRepository.save(reply);
            // createMessageReplyNotification(savedReply, message);
            return savedReply;
        } else {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }
    }

    private void createMessageNotification(Message message) {
        Notification notification = new Notification();
        notification.setTitle("New Message");
        notification.setMessage("You received a new message from " + message.getSender().getName());
        notification.setUser(message.getReceiver());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("NEW_MESSAGE");
        notificationService.createNotification(notification);
    }

    private void createMessageDeletedNotification(Message message) {
        Notification notification = new Notification();
        notification.setTitle("Message Deleted");
        notification.setMessage("A message from " + message.getSender().getName() + " was deleted");
        notification.setUser(message.getReceiver());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("MESSAGE_DELETED");
        notificationService.createNotification(notification);
    }

    private void createMessageEditedNotification(Message message) {
        Notification notification = new Notification();
        notification.setTitle("Message Edited");
        notification.setMessage("A message from " + message.getSender().getName() + " was edited");
        notification.setUser(message.getReceiver());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("MESSAGE_EDITED");
        notificationService.createNotification(notification);
    }

    private void createMessageReactionNotification(Message message) {
        Notification notification = new Notification();
        notification.setTitle("Message Reaction");
        notification.setMessage("Someone reacted to your message");
        notification.setUser(message.getSender());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("MESSAGE_REACTION");
        notificationService.createNotification(notification);
    }

    private void createMessageReplyNotification(Message reply, Message originalMessage) {
        Notification notification = new Notification();
        notification.setTitle("Message Reply");
        notification.setMessage(reply.getSender().getName() + " replied to your message");
        notification.setUser(originalMessage.getSender());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("MESSAGE_REPLY");
        notificationService.createNotification(notification);
    }
}
