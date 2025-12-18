package com.vynqtalk.server.service.user;

import com.vynqtalk.server.repository.UserSettingsRepository;
import com.vynqtalk.server.service.message.GroupMessageService;
import com.vynqtalk.server.service.message.MessageService;
import com.vynqtalk.server.service.websocket.WebSocketSessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.exceptions.SystemException;
import com.vynqtalk.server.exceptions.UserNotFoundException;
import com.vynqtalk.server.repository.UserRepository;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.users.UserSettings;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.repository.GroupRepository;
import com.vynqtalk.server.repository.GroupMessageRepository;
import com.vynqtalk.server.repository.NotificationRepository;
import com.vynqtalk.server.repository.DeviceTokenRepository;
import com.vynqtalk.server.repository.UserLogRepository;

import java.time.Instant;
import java.util.List;

/**
 * Service for user-related operations, including authentication, user
 * management, and login attempt tracking.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final UserSettingsService userSettingsService;
    private final WebSocketSessionService webSocketSessionService;
    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final GroupMessageService groupMessageService;
    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserLogRepository userLogRepository;
    private final UserMapper userMapper;

    /**
     * Saves a new user, encoding the password and setting default fields.
     */
    @Transactional
    public UserDTO saveUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");
        user.setLastActive(Instant.now());
        user.setBio("No bio yet");
        user.setUserRole(user.getUserRole());
        
        return userMapper.toDTO(userRepository.save(user));
    }

    /**
     * Updates a user using a UserDTO.
     */
    @Transactional
    public User updateUser(UserDTO userDTO) {
        User user = this.getUserById(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setUserRole(userDTO.getUserRole());
        user.setName(userDTO.getName());
        user.setStatus(userDTO.getStatus());
        user.setLastActive(userDTO.getLastActive());
        return userRepository.save(user);
    }

    /**
     * Gets a user by ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException());
    }

    /**
     * Gets a user by email.
     */
    public UserDTO getUserByEmail(String email) {
        User user= userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        return userMapper.toDTO(user);
    }

    public boolean checkUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Updates a user by ID using a User entity.
     */
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAvatar(user.getAvatar());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID, handling all related data and constraints.
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        System.out.println("this is loggin level" + user);
        // 1. Check group admin status
        List<Group> adminGroups = groupRepository.findByAdmins_Id(id);
        for (Group group : adminGroups) {
            if (group.getAdmins().size() == 1) {
                throw new SystemException("Cannot delete user: user is the only admin in group '" + group.getName()
                        + "'. Assign a new admin first.");
            }
        }
        // 2. Delete all direct messages (sent or received)
        messageService.deleteAllMessagesByUserId(id);
        // 3. Delete all group messages sent by user
        groupMessageRepository.deleteBySenderId(id);
        // 4. Delete user settings
        userSettingsService.deleteUserSettings(user);
        // 5. Delete notifications
        notificationRepository.deleteByUserId(id);
        // 6. Delete device tokens
        deviceTokenRepository.deleteByUser(user);
        // 7. Remove user from all group members/admins
        List<com.vynqtalk.server.model.groups.Group> memberGroups = groupRepository.findByMembers_Id(id);
        for (com.vynqtalk.server.model.groups.Group group : memberGroups) {
            group.getMembers().removeIf(u -> u.getId().equals(id));
            group.getAdmins().removeIf(u -> u.getId().equals(id));
            groupRepository.save(group);
        }
        // 8. Log the deletion
        userLogRepository.save(new com.vynqtalk.server.model.users.UserLog(user.getEmail(), user.getName(),
                "DELETED ACCOUNT", java.time.Instant.now()));
        // 9. Delete the user
        userRepository.deleteById(id);
    }

    /**
     * Returns all users with their latest message.
     */
    public List<UserDTO> getAllUsersWithLatestMessage(String email) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setAvatar(user.getAvatar());
            dto.setPassword(null);
            dto.setEmail(user.getEmail());
            dto.setUserRole(user.getUserRole());
            dto.setStatus(user.getStatus());
            dto.setBio(user.getBio());
            dto.setLastActive(user.getLastActive());
            dto.setCreatedAt(user.getCreatedAt());
            Message latestMessage = messageService.getLatestMessageByUserId(user.getId(), currentUser.getId());
            dto.setLatestMessage(messageMapper.toDTO(latestMessage));
            // Set online status if allowed
            boolean online = false;
            try {
                UserSettings settings = userSettingsService.getUserSettings(user);
                if (settings.getShowOnlineStatus() != null && settings.getShowOnlineStatus()) {
                    online = webSocketSessionService.isUserOnline(user.getId());
                    dto.setOnline(online);
                }
            } catch (Exception ignored) {
            }
            return dto;
        }).toList();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUserWithUnreadMessages(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setPassword(null);
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setStatus(user.getStatus());
        dto.setBio(user.getBio());
        dto.setLastActive(user.getLastActive());
        dto.setCreatedAt(user.getCreatedAt());
        // Set unread messages
        dto.setUnreadMessages(messageService.getUnreadMessagesByUserId(user.getId())
                .stream().map(messageMapper::toDTO).toList());

        return dto;
    }
}
