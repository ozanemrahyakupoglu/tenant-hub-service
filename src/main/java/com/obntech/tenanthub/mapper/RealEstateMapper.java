package com.obntech.tenanthub.mapper;

import com.obntech.tenanthub.dto.request.RealEstateCreateRequest;
import com.obntech.tenanthub.dto.response.RealEstateResponse;
import com.obntech.tenanthub.entity.RealEstateEntity;
import org.springframework.stereotype.Component;

@Component
public class RealEstateMapper {

    public RealEstateResponse toResponse(RealEstateEntity entity) {
        return RealEstateResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .province(entity.getProvince())
                .district(entity.getDistrict())
                .neighborhood(entity.getNeighborhood())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public RealEstateEntity toEntity(RealEstateCreateRequest request) {
        RealEstateEntity entity = new RealEstateEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setProvince(request.getProvince());
        entity.setDistrict(request.getDistrict());
        entity.setNeighborhood(request.getNeighborhood());
        entity.setAddress(request.getAddress());
        return entity;
    }
}
