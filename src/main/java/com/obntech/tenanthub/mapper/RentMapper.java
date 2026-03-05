package com.obntech.tenanthub.mapper;

import com.obntech.tenanthub.dto.response.RentResponse;
import com.obntech.tenanthub.entity.RentEntity;
import org.springframework.stereotype.Component;

@Component
public class RentMapper {

    public RentResponse toResponse(RentEntity entity) {
        return RentResponse.builder()
                .id(entity.getId())
                .realEstateId(entity.getRealEstate().getId())
                .realEstateName(entity.getRealEstate().getName())
                .rentDate(entity.getRentDate())
                .rentAmount(entity.getRentAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .build();
    }
}
