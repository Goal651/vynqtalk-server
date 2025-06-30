package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.model.Group;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface GroupMapper {
    GroupDTO toDTO(Group group);
    Group toEntity(GroupDTO groupDTO);
    List<GroupDTO> toDTOs(List<Group> groups);
    List<Group> toEntities(List<GroupDTO> groupDTOs);
}
