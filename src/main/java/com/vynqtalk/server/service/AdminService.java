package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.model.Alert;
import com.vynqtalk.server.repository.AlertRepository;
import com.vynqtalk.server.repository.MessageRepository;
import com.vynqtalk.server.repository.GroupRepository;
import com.vynqtalk.server.repository.UserRepository;
import java.time.LocalDate;
import java.time.ZoneId;


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

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MessageRepository messageRepository;

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
        User user = userService.getUserById(id);
        user.setId(id);
        userService.updateUser(id, user);
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return groups.stream().map(groupMapper::toDTO).toList();
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getNewUsersThisMonth() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        Instant start = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return userRepository.countByCreatedAtAfter(start);
    }

    public long getTotalGroups() {
        return groupRepository.count();
    }

    public long getNewGroupsThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        Instant start = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return groupRepository.countByCreatedAtAfter(start);
    }

    public long getMessagesToday() {
        LocalDate today = LocalDate.now();
        Instant start = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return messageRepository.countByTimestampBetween(start, end);
    }

    public long getMessagesYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Instant start = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return messageRepository.countByTimestampBetween(start, end);
    }

    public List<Alert> getRecentAlerts(int limit) {
        return alertRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }
}
