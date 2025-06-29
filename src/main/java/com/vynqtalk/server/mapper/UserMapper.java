package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.User;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    List<UserDTO> toDTOs(List<User> users);
    List<User> toEntities(List<UserDTO> userDTOs);

}
