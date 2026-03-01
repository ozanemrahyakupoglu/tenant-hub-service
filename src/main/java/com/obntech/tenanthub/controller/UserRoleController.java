package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.UserRoleRequest;
import com.obntech.tenanthub.dto.response.UserRoleResponse;
import com.obntech.tenanthub.service.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-roles")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserRoleResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getByUserId(userId));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<UserRoleResponse>> getByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(userRoleService.getByRoleId(roleId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserRoleResponse> assign(
            @Valid @RequestBody UserRoleRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRoleService.assign(request, httpRequest.getRemoteAddr()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userRoleService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
