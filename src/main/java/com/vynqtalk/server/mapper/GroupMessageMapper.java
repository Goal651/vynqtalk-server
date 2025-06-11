package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.model.GroupMessage;

@Mapper(componentModel = "spring")
public interface GroupMessageMapper {
    GroupMessageDTO toDTO(GroupMessage groupMessage);
}
