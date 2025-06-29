package com.vynqtalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;


    // Add methods to handle group-related operations

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group save(Group group) {
        Group savedGroup = groupRepository.save(group);
        return savedGroup;
    }

    public void delete(Long id) {
        Group group = findById(id);
        if (group != null) {
            // Create notification for group deletion
            groupRepository.deleteById(id);
        }
    }

    public Group addMember(Group group, User user) {
        group.getMembers().add(user);
        Group savedGroup = groupRepository.save(group);
        // Create notification for new member
        return savedGroup;
    }

    public Group removeMember(Group group, User user) {
        // Create notification for removed member before removing them
        // Now remove the member
        group.getMembers().removeIf(m -> m.getId().equals(user.getId()));
        return groupRepository.save(group);
    }

}
