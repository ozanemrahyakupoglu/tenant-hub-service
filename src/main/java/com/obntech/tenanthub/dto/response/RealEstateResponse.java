package com.obntech.tenanthub.dto.response;

import com.obntech.tenanthub.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateResponse {

    private Long id;
    private String name;
    private String description;
    private String type;
    private String province;
    private String district;
    private String neighborhood;
    private String address;
    private Long tenantId;
    private String tenantName;
    private Long landlordId;
    private String landlordName;
    private Status status;
    private LocalDateTime createdDate;
    private String createdBy;
}
