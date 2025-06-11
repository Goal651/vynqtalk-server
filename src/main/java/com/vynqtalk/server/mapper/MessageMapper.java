package com.vynqtalk.server.mapper;

import com.vynqtalk.server.dto.messages.MessageDTO;
import com.vynqtalk.server.model.Message;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDTO toDTO(Message message);
}