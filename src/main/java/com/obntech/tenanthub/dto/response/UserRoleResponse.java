package com.obntech.tenanthub.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponse {

    private Long id;
    private Long userId;
    private String username;
    private Long roleId;
    private String roleName;
    private String assignedBy;
    private LocalDateTime assignedDate;
}
