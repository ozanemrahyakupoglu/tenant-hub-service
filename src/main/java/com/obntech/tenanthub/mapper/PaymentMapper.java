package com.obntech.tenanthub.mapper;

import com.obntech.tenanthub.dto.response.PaymentResponse;
import com.obntech.tenanthub.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(PaymentEntity entity) {
        return PaymentResponse.builder()
                .id(entity.getId())
                .rentId(entity.getRent().getId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .paymentDate(entity.getPaymentDate())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .build();
    }
}
