package com.obntech.tenanthub.dto.response;

import com.obntech.tenanthub.enums.PermissionAction;
import com.obntech.tenanthub.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private String name;
    private String description;
    private String module;
    private PermissionAction action;
    private Status status;
    private LocalDateTime createdDate;
    private String createdBy;
}
