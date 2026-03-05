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
public class RentResponse {

    private Long id;
    private Long realEstateId;
    private String realEstateName;
    private LocalDateTime rentDate;
    private BigDecimal rentAmount;
    private String currency;
    private Status status;
    private LocalDateTime createdDate;
    private String createdBy;
}
