package com.obntech.tenanthub.mapper;

import com.obntech.tenanthub.dto.request.PermissionCreateRequest;
import com.obntech.tenanthub.dto.response.PermissionResponse;
import com.obntech.tenanthub.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toResponse(PermissionEntity entity) {
        return PermissionResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .module(entity.getModule())
                .action(entity.getAction())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public PermissionEntity toEntity(PermissionCreateRequest request) {
        PermissionEntity entity = new PermissionEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setModule(request.getModule());
        entity.setAction(request.getAction());
        return entity;
    }
}
