package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.exceptions.GroupNotFoundException;
import com.vynqtalk.server.model.enums.NotificationType;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.system.Notification;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.repository.GroupRepository;

@Service
public class GroupService {

    private final NotificationService notificationService;

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository, NotificationService notificationService) {
        this.groupRepository = groupRepository;
        this.notificationService = notificationService;
    }

    /**
     * Returns all groups.
     */
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    /**
     * Finds a group by ID or throws if not found.
     * 
     * @param id the group ID
     * @return the group
     * @throws GroupNotFoundException if not found
     */
    public Group findById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException());
    }

    /**
     * Saves a group (create or update).
     */
    @Transactional
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    /**
     * Deletes a group by ID.
     * 
     * @param id the group ID
     * @throws GroupNotFoundException if not found
     */
    @Transactional
    public void delete(Long id) {
        findById(id);
        // Create notification for group deletion if needed
        groupRepository.deleteById(id);
    }

    /**
     * Adds a member to a group.
     * 
     * @param group the group
     * @param user  the user to add
     * @return the updated group
     */
    @Transactional
    public Group addMember(Group group, User user) {
        List<User> members = group.getMembers();

        Notification notification = new Notification();
        notification.setIsRead(false);
        notification.setTitle("New member added");
        notification.setType(NotificationType.GROUP_UPDATE);
        notification.setTimestamp(Instant.now());
        members.forEach((member) -> {
            notification.setMessage("User " + user.getName() + " added to group " + group.getName());
            notification.setUser(member);
            notificationService.createNotification(notification);
        });
        notification.setMessage("You have been added to group " + group.getName());
        notification.setUser(user);
        members.add(user);
        notificationService.createNotification(notification);
        return groupRepository.save(group);
    }

    /**
     * Removes a member from a group.
     * 
     * @param group the group
     * @param user  the user to remove
     * @return the updated group
     */
    @Transactional
    public Group removeMember(Group group, User user) {
        List<User> members = group.getMembers();

        Notification notification = new Notification();
        notification.setIsRead(false);
        notification.setTitle("Member removed");
        notification.setType(NotificationType.GROUP_UPDATE);
        notification.setTimestamp(Instant.now());
        members.forEach((member) -> {
            notification.setMessage("User " + user.getName() + " removed from group " + group.getName());
            notification.setUser(member);
            notificationService.createNotification(notification);
        });
        notification.setMessage("You have been removed from group " + group.getName());
        notification.setUser(user);
        members.remove(user);

        notificationService.createNotification(notification);
        return groupRepository.save(group);
    }

}
