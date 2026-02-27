package com.obntech.tenanthub.mapper;

import com.obntech.tenanthub.dto.request.RoleCreateRequest;
import com.obntech.tenanthub.dto.response.RoleResponse;
import com.obntech.tenanthub.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse toResponse(RoleEntity entity) {
        return RoleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public RoleEntity toEntity(RoleCreateRequest request) {
        RoleEntity entity = new RoleEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }
}
