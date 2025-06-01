package com.vynqtalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.Group;
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
        return groupRepository.save(group);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
    
}
