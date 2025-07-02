package com.vynqtalk.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vynqtalk.server.model.messages.GroupMessage;
import com.vynqtalk.server.model.messages.Reaction;
import com.vynqtalk.server.repository.GroupMessageRepository;
import com.vynqtalk.server.error.GroupMessageNotFoundException;

@Service
public class GroupMessageService {

    private final GroupMessageRepository groupMessageRepository;

    public GroupMessageService(GroupMessageRepository groupMessageRepository) {
        this.groupMessageRepository = groupMessageRepository;
    }

    /**
     * Saves a group message.
     */
    @Transactional
    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        return groupMessageRepository.save(groupMessage);
    }

    /**
     * Gets a group message by ID.
     * 
     * @throws GroupMessageNotFoundException if not found
     */
    public GroupMessage getGroupMessageById(Long id) {
        return groupMessageRepository.findById(id)
                .orElseThrow(() -> new GroupMessageNotFoundException("Group message not found with id: " + id));
    }

    /**
     * Gets all group messages for a group.
     */
    public List<GroupMessage> getAllGroupMessages(Long groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }

    /**
     * Deletes a group message by ID.
     * 
     * @throws GroupMessageNotFoundException if not found
     */
    @Transactional
    public void deleteGroupMessage(Long id) {
        groupMessageRepository.findById(id)
                .orElseThrow(() -> new GroupMessageNotFoundException("Group message not found with id: " + id));
        groupMessageRepository.deleteById(id);
    }

    /**
     * Updates a group message by ID.
     * 
     * @throws GroupMessageNotFoundException if not found
     */
    @Transactional
    public GroupMessage updateGroupMessage(Long id, GroupMessage updatedGroupMessage) {
        GroupMessage groupMessage = groupMessageRepository.findById(id)
                .orElseThrow(() -> new GroupMessageNotFoundException("Group message not found with id: " + id));
        groupMessage.setContent(updatedGroupMessage.getContent());
        groupMessage.setSender(updatedGroupMessage.getSender());
        groupMessage.setTimestamp(updatedGroupMessage.getTimestamp());
        return groupMessageRepository.save(groupMessage);
    }

    /**
     * Reacts to a group message by ID.
     * 
     * @throws GroupMessageNotFoundException if not found
     */
    @Transactional
    public GroupMessage reactToMessage(Long messageId, List<Reaction> reactions) {
        GroupMessage message = groupMessageRepository.findById(messageId)
                .orElseThrow(() -> new GroupMessageNotFoundException("Group message not found with id: " + messageId));
        message.setReactions(reactions);
        return groupMessageRepository.save(message);
    }
}
