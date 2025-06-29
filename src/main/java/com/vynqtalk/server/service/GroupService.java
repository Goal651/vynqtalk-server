package com.vynqtalk.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.GroupRepository;
import com.vynqtalk.server.error.GroupNotFoundException;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Returns all groups.
     */
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    /**
     * Finds a group by ID or throws if not found.
     * @param id the group ID
     * @return the group
     * @throws GroupNotFoundException if not found
     */
    public Group findById(Long id) {
        return groupRepository.findById(id)
            .orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + id));
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
     * @param id the group ID
     * @throws GroupNotFoundException if not found
     */
    @Transactional
    public void delete(Long id) {
        Group group = findById(id);
        // Create notification for group deletion if needed
        groupRepository.deleteById(id);
    }

    /**
     * Adds a member to a group.
     * @param group the group
     * @param user the user to add
     * @return the updated group
     */
    @Transactional
    public Group addMember(Group group, User user) {
        group.getMembers().add(user);
        // Create notification for new member if needed
        return groupRepository.save(group);
    }

    /**
     * Removes a member from a group.
     * @param group the group
     * @param user the user to remove
     * @return the updated group
     */
    @Transactional
    public Group removeMember(Group group, User user) {
        // Create notification for removed member before removing them if needed
        group.getMembers().removeIf(m -> m.getId().equals(user.getId()));
        return groupRepository.save(group);
    }

}
