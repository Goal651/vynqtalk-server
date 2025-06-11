package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.notifications.NotificationDTO;
import com.vynqtalk.server.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDTO(Notification notification);
    
}
