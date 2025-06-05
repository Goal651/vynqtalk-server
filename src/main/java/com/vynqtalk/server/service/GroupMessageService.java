package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.Notification;
import com.vynqtalk.server.repository.GroupMessageRepository;

@Service
public class GroupMessageService {

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private NotificationService notificationService;

    // Add methods to handle group message operations, such as saving messages,
    // retrieving messages, etc.
    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        GroupMessage savedMessage = groupMessageRepository.save(groupMessage);
        createGroupMessageNotification(savedMessage);
        return savedMessage;
    }

    public Optional<GroupMessage> getGroupMessageById(Long id) {
        return groupMessageRepository.findById(id);
    }

    public List<GroupMessage> getAllGroupMessages(Long groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }

    public void deleteGroupMessage(Long id) {
        Optional<GroupMessage> messageOpt = groupMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            GroupMessage message = messageOpt.get();
            createGroupMessageDeletedNotification(message);
            groupMessageRepository.deleteById(id);
        }
    }

    public GroupMessage updateGroupMessage(Long id, GroupMessage updatedGroupMessage) {
        Optional<GroupMessage> existingGroupMessage = groupMessageRepository.findById(id);
        if (existingGroupMessage.isPresent()) {
            GroupMessage groupMessage = existingGroupMessage.get();
            groupMessage.setContent(updatedGroupMessage.getContent());
            groupMessage.setSender(updatedGroupMessage.getSender());
            groupMessage.setTimestamp(updatedGroupMessage.getTimestamp());
            GroupMessage savedMessage = groupMessageRepository.save(groupMessage);
            createGroupMessageEditedNotification(savedMessage);
            return savedMessage;
        }
        return null;
    }

    public GroupMessage reactToMessage(Long messageId, List<String> reactions) {
        Optional<GroupMessage> optionalMessage = groupMessageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            GroupMessage message = optionalMessage.get();
            message.setReactions(reactions);
            GroupMessage savedMessage = groupMessageRepository.save(message);
            createGroupMessageReactionNotification(savedMessage);
            return savedMessage;
        }
        return null;
    }

    private void createGroupMessageNotification(GroupMessage message) {
        message.getGroup().getMembers().forEach(member -> {
            if (!member.getId().equals(message.getSender().getId())) {
                Notification notification = new Notification();
                notification.setTitle("New Group Message");
                notification.setMessage(message.getSender().getName() + " sent a message in " + message.getGroup().getName());
                notification.setUser(member);
                notification.setTimestamp(Instant.now());
                notification.setIsRead(false);
                notification.setType("NEW_GROUP_MESSAGE");
                notificationService.createNotification(notification);
            }
        });
    }

    private void createGroupMessageDeletedNotification(GroupMessage message) {
        message.getGroup().getMembers().forEach(member -> {
            if (!member.getId().equals(message.getSender().getId())) {
                Notification notification = new Notification();
                notification.setTitle("Group Message Deleted");
                notification.setMessage("A message from " + message.getSender().getName() + " was deleted in " + message.getGroup().getName());
                notification.setUser(member);
                notification.setTimestamp(Instant.now());
                notification.setIsRead(false);
                notification.setType("GROUP_MESSAGE_DELETED");
                notificationService.createNotification(notification);
            }
        });
    }

    private void createGroupMessageEditedNotification(GroupMessage message) {
        message.getGroup().getMembers().forEach(member -> {
            if (!member.getId().equals(message.getSender().getId())) {
                Notification notification = new Notification();
                notification.setTitle("Group Message Edited");
                notification.setMessage(message.getSender().getName() + " edited their message in " + message.getGroup().getName());
                notification.setUser(member);
                notification.setTimestamp(Instant.now());
                notification.setIsRead(false);
                notification.setType("GROUP_MESSAGE_EDITED");
                notificationService.createNotification(notification);
            }
        });
    }

    private void createGroupMessageReactionNotification(GroupMessage message) {
        Notification notification = new Notification();
        notification.setTitle("Group Message Reaction");
        notification.setMessage("Someone reacted to your message in " + message.getGroup().getName());
        notification.setUser(message.getSender());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("GROUP_MESSAGE_REACTION");
        notificationService.createNotification(notification);
    }
}
