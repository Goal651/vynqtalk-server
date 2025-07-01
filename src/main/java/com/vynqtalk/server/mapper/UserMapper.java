package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.users.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);
    List<UserDTO> toDTOs(List<User> users);
    @Mapping(target = "password", ignore = true)
    List<User> toEntities(List<UserDTO> userDTOs);

}
