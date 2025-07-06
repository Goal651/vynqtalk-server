package com.vynqtalk.server.dto.user;

import java.util.List;

import com.vynqtalk.server.dto.group.GroupDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportUserDTO {
    UserDTO user;
    List<GroupDTO> groups;
}
