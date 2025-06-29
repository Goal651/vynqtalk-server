package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.messages.GroupMessageDTO;
import com.vynqtalk.server.model.GroupMessage;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMessageMapper {
    GroupMessageDTO toDTO(GroupMessage groupMessage);
    GroupMessage toEntity(GroupMessageDTO groupMessageDTO);
    List<GroupMessageDTO> toDTOs(List<GroupMessage> groupMessages);
    List<GroupMessage> toEntities(List<GroupMessageDTO> groupMessageDTOs);
}
