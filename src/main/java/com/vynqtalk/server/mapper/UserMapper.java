package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

}
