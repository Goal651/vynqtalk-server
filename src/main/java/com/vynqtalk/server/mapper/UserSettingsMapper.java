package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;
import com.vynqtalk.server.dto.user.UserSettingsDTO;
import com.vynqtalk.server.model.UserSettings;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSettingsMapper {
    UserSettingsDTO toDTO(UserSettings userSettings);
    UserSettings toEntity(UserSettingsDTO userSettingsDTO);
    List<UserSettingsDTO> toDTOs(List<UserSettings> userSettingsList);
    List<UserSettings> toEntities(List<UserSettingsDTO> userSettingsDTOList);
} 