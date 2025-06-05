package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.Notification;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private NotificationService notificationService;

    // Add methods to handle group-related operations

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group save(Group group) {
        Group savedGroup = groupRepository.save(group);
        // Create notification for group creation
        if (group.getId() == null) {
            createGroupCreationNotification(savedGroup);
        }
        return savedGroup;
    }

    public void delete(Long id) {
        Group group = findById(id);
        if (group != null) {
            // Create notification for group deletion
            createGroupDeletionNotification(group);
            groupRepository.deleteById(id);
        }
    }

    public Group addMember(Group group, User user) {
        group.getMembers().add(user);
        Group savedGroup = groupRepository.save(group);
        // Create notification for new member
        createMemberAddedNotification(savedGroup, user);
        return savedGroup;
    }

    public Group removeMember(Group group, User user) {
        // Create notification for removed member before removing them
        createMemberRemovedNotification(group, user);
        // Now remove the member
        group.getMembers().removeIf(m -> m.getId().equals(user.getId()));
        return groupRepository.save(group);
    }

    private void createGroupCreationNotification(Group group) {
        Notification notification = new Notification();
        notification.setTitle("New Group Created");
        notification.setMessage("Group '" + group.getName() + "' has been created");
        notification.setUser(group.getCreatedBy());
        notification.setTimestamp(Instant.now());
        notification.setIsRead(false);
        notification.setType("GROUP_CREATED");
        notificationService.createNotification(notification);
    }

    private void createGroupDeletionNotification(Group group) {
        for (User member : group.getMembers()) {
            Notification notification = new Notification();
            notification.setTitle("Group Deleted");
            notification.setMessage("Group '" + group.getName() + "' has been deleted");
            notification.setUser(member);
            notification.setTimestamp(Instant.now());
            notification.setIsRead(false);
            notification.setType("GROUP_DELETED");
            notificationService.createNotification(notification);
        }
    }

    private void createMemberAddedNotification(Group group, User newMember) {
        // Notify the new member
        Notification memberNotification = new Notification();
        memberNotification.setTitle("Added to Group");
        memberNotification.setMessage("You have been added to group '" + group.getName() + "'");
        memberNotification.setUser(newMember);
        memberNotification.setTimestamp(Instant.now());
        memberNotification.setIsRead(false);
        memberNotification.setType("GROUP_MEMBER_ADDED");
        notificationService.createNotification(memberNotification);

        // Notify other group members
        for (User member : group.getMembers()) {
            if (!member.getId().equals(newMember.getId())) {
                Notification notification = new Notification();
                notification.setTitle("New Group Member");
                notification.setMessage(newMember.getName() + " has joined group '" + group.getName() + "'");
                notification.setUser(member);
                notification.setTimestamp(Instant.now());
                notification.setIsRead(false);
                notification.setType("GROUP_MEMBER_ADDED");
                notificationService.createNotification(notification);
            }
        }
    }

    private void createMemberRemovedNotification(Group group, User removedMember) {
        System.out.println("Creating member removed notification for group: "+group);
        System.out.println("Removed member: "+removedMember);
        // Notify the removed member
        Notification memberNotification = new Notification();
        memberNotification.setTitle("Removed from Group");
        memberNotification.setMessage("You have been removed from group '" + group.getName() + "'");
        memberNotification.setUser(removedMember);
        memberNotification.setTimestamp(Instant.now());
        memberNotification.setIsRead(false);
        memberNotification.setType("GROUP_MEMBER_REMOVED");
        notificationService.createNotification(memberNotification);

        // Notify other group members
        for (User member : group.getMembers()) {
            Notification notification = new Notification();
            notification.setTitle("Group Member Removed");
            notification.setMessage(removedMember.getName() + " has been removed from group '" + group.getName() + "'");
            notification.setUser(member);
            notification.setTimestamp(Instant.now());
            notification.setIsRead(false);
            notification.setType("GROUP_MEMBER_REMOVED");
            notificationService.createNotification(notification);
        }
    }
}
