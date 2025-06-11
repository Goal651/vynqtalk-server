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

    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        GroupMessage savedMessage = groupMessageRepository.save(groupMessage);
        return savedMessage;
    }

    public GroupMessage getGroupMessageById(Long id) {
        return groupMessageRepository.findById(id).get();
    }

    public List<GroupMessage> getAllGroupMessages(Long groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }

    public void deleteGroupMessage(Long id) {
        Optional<GroupMessage> messageOpt = groupMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            groupMessageRepository.deleteById(id);
        }
    }

    public GroupMessage updateGroupMessage(Long id, GroupMessage updatedGroupMessage) {
        Optional<GroupMessage> existingGroupMessage = groupMessageRepository.findById(id);
        if (existingGroupMessage.isPresent()) {
            GroupMessage groupMessage = existingGroupMessage.get();
            groupMessage.setContent(updatedGroupMessage.getContent());
            groupMessage.setSender(updatedGroupMessage.getSender());
            groupMessage.setTimestamp(updatedGroupMessage.getTimestamp());
            GroupMessage savedMessage = groupMessageRepository.save(groupMessage);
            return savedMessage;
        }
        return null;
    }

    public GroupMessage reactToMessage(Long messageId, List<String> reactions) {
        Optional<GroupMessage> optionalMessage = groupMessageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            GroupMessage message = optionalMessage.get();
            message.setReactions(reactions);
            GroupMessage savedMessage = groupMessageRepository.save(message);
            return savedMessage;
        }
        return null;
    }
}
