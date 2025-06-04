package com.vynqtalk.server.mapper;

import com.vynqtalk.server.model.Message;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.MessageDTO;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDTO toDTO(Message message);
}