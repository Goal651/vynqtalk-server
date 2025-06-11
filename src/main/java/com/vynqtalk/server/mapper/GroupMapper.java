package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.model.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDTO toDTO(Group group);
}
