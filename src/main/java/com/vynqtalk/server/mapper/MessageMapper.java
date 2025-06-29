package com.vynqtalk.server.mapper;

import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.model.Message;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDTO toDTO(Message message);
    Message toEntity(MessageDTO messageDTO);
    List<MessageDTO> toDTOs(List<Message> messages);
    List<Message> toEntities(List<MessageDTO> messageDTOs);
}