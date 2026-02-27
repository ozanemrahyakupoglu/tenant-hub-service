package com.obntech.tenanthub.dto.response;

import com.obntech.tenanthub.enums.PermissionAction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionResponse {

    private Long id;
    private Long roleId;
    private String roleName;
    private Long permissionId;
    private String permissionName;
    private String module;
    private PermissionAction action;
    private LocalDateTime createdDate;
}
