package com.vynqtalk.server.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.mapper.GroupMapper;
import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.enums.UserRole;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.system.Alert;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.repository.AlertRepository;
import com.vynqtalk.server.repository.MessageRepository;
import com.vynqtalk.server.repository.GroupRepository;
import com.vynqtalk.server.repository.UserRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import com.vynqtalk.server.error.UserNotFoundException;

/**
 * Service for admin-related operations, including user, group, and alert
 * management,
 * as well as dashboard statistics.
 */
@Service
public class AdminService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final GroupService groupService;
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;

    public AdminService(UserService userService, UserMapper userMapper, GroupMapper groupMapper,
            GroupService groupService, AlertRepository alertRepository, UserRepository userRepository,
            GroupRepository groupRepository, MessageRepository messageRepository) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.groupMapper = groupMapper;
        this.groupService = groupService;
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Creates a new admin user.
     */
    @Transactional
    public UserDTO createAdmin(User user) {
        user.setUserRole(UserRole.USER);
        user.setStatus("active");
        user.setLastActive(Instant.now());
        user.setBio("No bio yet");
        userService.saveUser(user);
        return userMapper.toDTO(user);
    }

    /**
     * Returns all users as DTOs.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(userMapper::toDTO).toList();
    }

    /**
     * Deletes a user by ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    /**
     * Updates a user by ID using a UserDTO.
     * 
     * @return the updated UserDTO
     * @throws UserNotFoundException if not found
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setId(id);
        userService.updateUser(user);
        return userMapper.toDTO(user);
    }

    /**
     * Returns all groups as DTOs.
     */
    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return groups.stream().map(groupMapper::toDTO).toList();
    }

    /**
     * Returns all alerts.
     */
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    /**
     * Returns the total number of users.
     */
    public long getTotalUsers() {
        return userRepository.count();
    }

    /**
     * Returns the number of new users this month.
     */
    public long getNewUsersThisMonth() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        Instant start = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return userRepository.countByCreatedAtAfter(start);
    }

    /**
     * Returns the total number of groups.
     */
    public long getTotalGroups() {
        return groupRepository.count();
    }

    /**
     * Returns the number of new groups created this week.
     */
    public long getNewGroupsThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        Instant start = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return groupRepository.countByCreatedAtAfter(start);
    }

    /**
     * Returns the number of messages sent today.
     */
    public long getMessagesToday() {
        LocalDate today = LocalDate.now();
        Instant start = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return messageRepository.countByTimestampBetween(start, end);
    }

    /**
     * Returns the number of messages sent yesterday.
     */
    public long getMessagesYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Instant start = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return messageRepository.countByTimestampBetween(start, end);
    }

    /**
     * Returns the most recent alerts, limited by the given number.
     */
    public List<Alert> getRecentAlerts(int limit) {
        return alertRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }
}
