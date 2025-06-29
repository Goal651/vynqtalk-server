package com.vynqtalk.server.mapper;

import org.mapstruct.Mapper;

import com.vynqtalk.server.dto.notifications.NotificationDTO;
import com.vynqtalk.server.model.Notification;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDTO(Notification notification);
    Notification toEntity(NotificationDTO notificationDTO);
    List<NotificationDTO> toDTOs(List<Notification> notifications);
    List<Notification> toEntities(List<NotificationDTO> notificationDTOs);
}
