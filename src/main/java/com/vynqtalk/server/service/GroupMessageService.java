package com.vynqtalk.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.repository.GroupMessageRepository;

@Service
public class GroupMessageService {

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    // Add methods to handle group message operations, such as saving messages,
    // retrieving messages, etc.
    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        return groupMessageRepository.save(groupMessage);
    }

    public Optional<GroupMessage> getGroupMessageById(Long id) {
        return groupMessageRepository.findById(id);
    }

    public List<GroupMessage> getAllGroupMessages(Long groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }

    public void deleteGroupMessage(Long id) {
        groupMessageRepository.deleteById(id);
    }

    public GroupMessage updateGroupMessage(Long id, GroupMessage updatedGroupMessage) {
        Optional<GroupMessage> existingGroupMessage = groupMessageRepository.findById(id);
        if (existingGroupMessage.isPresent()) {
            GroupMessage groupMessage = existingGroupMessage.get();
            groupMessage.setContent(updatedGroupMessage.getContent());
            groupMessage.setSender(updatedGroupMessage.getSender());
            groupMessage.setTimestamp(updatedGroupMessage.getTimestamp());
            return groupMessageRepository.save(groupMessage);
        }
        return null; // or throw an exception
    }

    public GroupMessage reactToMessage(Long messageId, List<String> reactions) {
        Optional<GroupMessage> optionalMessage = groupMessageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            GroupMessage message = optionalMessage.get();
            message.setReactions(reactions);
            return groupMessageRepository.save(message);
        }
        return null; // or throw an exception
    }
}
