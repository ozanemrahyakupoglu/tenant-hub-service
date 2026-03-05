package com.obntech.tenanthub.dto.response;

import com.obntech.tenanthub.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long rentId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime paymentDate;
    private Status status;
    private LocalDateTime createdDate;
    private String createdBy;
}
