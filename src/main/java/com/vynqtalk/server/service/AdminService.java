package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.dto.GroupDTO;
import com.vynqtalk.server.dto.UserDTO;
import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.mapper.UserMapper;

@Service
public class AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupService groupService;

    public UserDTO createAdmin(User user) {
        user.setIsAdmin(true);
        user.setStatus("active");
        user.setLastActive(Instant.now());
        user.setBio("No bio yet");
        userService.saveUser(user);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> adminUserDTOs = users.stream().map(userMapper::toDTO).toList();
        return adminUserDTOs;
    }

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    public void updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setId(id);
        userService.updateUser(id, user);
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return groups.stream().map(groupMapper::toDTO).toList();
    }
}
