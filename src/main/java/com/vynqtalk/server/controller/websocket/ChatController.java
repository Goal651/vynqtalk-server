package com.vynqtalk.server.controller.websocket;

import com.vynqtalk.server.config.websocket.WebSocketInterceptor;
import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.dto.response.WsResponse;
import com.vynqtalk.server.dto.websocket.ChatGroupMessage;
import com.vynqtalk.server.dto.websocket.ChatGroupMessageReply;
import com.vynqtalk.server.dto.websocket.ChatMessage;
import com.vynqtalk.server.dto.websocket.ChatMessageEdit;
import com.vynqtalk.server.dto.websocket.ChatMessageReply;
import com.vynqtalk.server.dto.websocket.ReactMessage;
import com.vynqtalk.server.mapper.GroupMessageMapper;
import com.vynqtalk.server.mapper.MessageMapper;
import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.model.groups.Group;
import com.vynqtalk.server.model.messages.GroupMessage;
import com.vynqtalk.server.service.group.GroupService;
import com.vynqtalk.server.service.message.GroupMessageService;
import com.vynqtalk.server.service.message.MessageService;
import com.vynqtalk.server.service.notification.NotificationService;
import com.vynqtalk.server.service.user.UserService;

import java.time.Instant;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final GroupMessageService groupMessageService;
    private final MessageMapper messageMapper;
    private final GroupMessageMapper groupMessageMapper;
    private final UserService userService;
    private final GroupService groupService;
    private final NotificationService notificationService;
    private final WebSocketInterceptor webSocketInterceptor;

    public ChatController(MessageService messageService, GroupMessageService groupMessageService,
            MessageMapper messageMapper, GroupMessageMapper groupMessageMapper, UserService userService,
            GroupService groupService, NotificationService notificationService,
            WebSocketInterceptor webSocketInterceptor) {
        this.messageService = messageService;
        this.groupMessageService = groupMessageService;
        this.messageMapper = messageMapper;
        this.groupMessageMapper = groupMessageMapper;
        this.userService = userService;
        this.groupService = groupService;
        this.notificationService = notificationService;
        this.webSocketInterceptor = webSocketInterceptor;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public WsResponse<MessageDTO> receiveMessage(@Payload ChatMessage message) {
        try {
            System.out.println(message + "For testing       \n\n\n\n");
            Message savedMessage = new Message();
            User sender = userService.getUserById(message.getSenderId());
            User receiver = userService.getUserById(message.getReceiverId());

            savedMessage.setSender(sender);
            savedMessage.setReceiver(receiver);
            savedMessage.setContent(message.getContent());
            savedMessage.setEdited(false);
            savedMessage.setType(message.getType());
            savedMessage.setTimestamp(Instant.now());
            savedMessage.setReactions(List.of());
            savedMessage.setFileName(message.getFileName());

            Message saved = messageService.saveMessage(savedMessage);

            // Check if receiver is online using WebSocketInterceptor
            String receiverSessionId = webSocketInterceptor.getSessionIdByEmail(receiver.getEmail());
            if (receiverSessionId == null) {
                // User is offline, send push notification
                notificationService.sendPushNotificationToUser(receiver, "New message from " + sender.getName(),
                        message.getContent());
            }

            return new WsResponse<>(true, messageMapper.toDTO(saved), "Message Sent Successfully");
        } catch (Error e) {
            System.out.println("This is error handling " + e.getLocalizedMessage());
            return new WsResponse<>(false, null, "There have been error" + e.getMessage());
        }
    }

    @MessageMapping("/chat.sendMessageReply")
    @SendTo("/topic/messages")
    public WsResponse<MessageDTO> replyMessage(@Payload ChatMessageReply message) {
        Message savedMessage = new Message();
        User sender = userService.getUserById(message.getSenderId());
        User receiver = userService.getUserById(message.getSenderId());
        Message replyTo = messageService.getMessageById(message.getReplyToId());
        savedMessage.setSender(sender);
        savedMessage.setReceiver(receiver);
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyTo(replyTo);
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        savedMessage = messageService.saveMessage(savedMessage);
        return new WsResponse<>(true, messageMapper.toDTO(savedMessage), "Replied successfully");
    }

    @MessageMapping("/chat.sendMessageReaction")
    @SendTo("/topic/reactions")
    public WsResponse<MessageDTO> reactToMessage(@Payload ReactMessage message) {
        Message exist = messageService.getMessageById(message.getMessageId());
        exist.setReactions(message.getReactions());
        Message savedMessage = messageService.saveMessage(exist);
        return new WsResponse<>(true, messageMapper.toDTO(savedMessage), "Reacted successfully");
    }

    @MessageMapping("/chat.deleteMessage")
    @SendTo("/topic/messageDeletion")
    public WsResponse<Long> deleteMessage(@Payload Long id) {
        messageService.deleteMessage(id);
        return new WsResponse<>(true, id, "Message Deleted successfully");
    }

    @MessageMapping("/chat.editMessage")
    @SendTo("/topic/messageEdition")
    public WsResponse<MessageDTO> editMessage(@Payload ChatMessageEdit newMessage) {
        Message oldMessage = messageService.getMessageById(newMessage.getId());
        oldMessage.setContent(newMessage.getNewMessage());
        oldMessage.setEdited(true);
        messageService.saveMessage(oldMessage);
        return new WsResponse<>(true, messageMapper.toDTO(oldMessage), "Message edited successfully");
    }

    // Group socket controller

    @MessageMapping("/group.sendMessage")
    @SendTo("/topic/groupMessages")
    public WsResponse<GroupMessageDTO> receiveGroupMessage(@Payload ChatGroupMessage message) {
        User sender = userService.getUserById(message.getSenderId());
        Group group = groupService.findById(message.getGroupId());
        GroupMessage savedMessage = new GroupMessage();
        savedMessage.setSender(sender);
        savedMessage.setGroup(group); 
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        GroupMessage saved = groupMessageService.saveGroupMessage(savedMessage);
        return new WsResponse<>(true, groupMessageMapper.toDTO(saved), "Sent message");
    }

    @MessageMapping("/group.sendMessageReply")
    @SendTo("/topic/groupMessages")
    public WsResponse<GroupMessageDTO> replyGroupMessage(@Payload ChatGroupMessageReply message) {
        GroupMessage savedMessage = new GroupMessage();
        User sender = userService.getUserById(message.getSenderId());
        Group group = groupService.findById(message.getGroupId());
        GroupMessage replyTo = groupMessageService.getGroupMessageById(message.getReplyToId());
        savedMessage.setSender(sender);
        savedMessage.setGroup(group);
        savedMessage.setContent(message.getContent());
        savedMessage.setReplyTo(replyTo);
        savedMessage.setEdited(false);
        savedMessage.setType(message.getType());
        savedMessage.setTimestamp(Instant.now());
        savedMessage.setReactions(List.of());
        savedMessage.setFileName(message.getFileName());
        savedMessage = groupMessageService.saveGroupMessage(savedMessage);
        return new WsResponse<>(true, groupMessageMapper.toDTO(savedMessage), "Replied successfully");
    }

    @MessageMapping("/group.sendMessageReaction")
    @SendTo("/topic/groupReactions")
    public WsResponse<GroupMessageDTO> reactToGroupMessage(@Payload ReactMessage message) {
        GroupMessage groupMessage = groupMessageService.getGroupMessageById(message.getMessageId());
        groupMessage.setReactions(message.getReactions());
        GroupMessage saved = groupMessageService.saveGroupMessage(groupMessage);
        return new WsResponse<>(true, groupMessageMapper.toDTO(saved), "Reacted successfully");
    }

    @MessageMapping("/group.deleteMessage")
    @SendTo("/topic/groupMessageDeletion")
    public WsResponse<Long> deleteGroupMessage(@Payload Long id) {
        groupMessageService.deleteGroupMessage(id);
        return new WsResponse<>(true, id, "Group message deleted successfully");
    }

    @MessageMapping("/group.editMessage")
    @SendTo("/topic/groupMessageEdition")
    public WsResponse<GroupMessageDTO> editGroupMessage(@Payload ChatMessageEdit newMessage) {
        GroupMessage oldMessage = groupMessageService.getGroupMessageById(newMessage.getId());
        oldMessage.setContent(newMessage.getNewMessage());
        oldMessage.setEdited(true);
        GroupMessage saved = groupMessageService.saveGroupMessage(oldMessage);
        return new WsResponse<>(true, groupMessageMapper.toDTO(saved), "Group message edited successfully");
    }
}
