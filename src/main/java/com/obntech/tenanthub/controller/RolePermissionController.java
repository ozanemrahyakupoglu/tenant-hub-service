package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.RolePermissionRequest;
import com.obntech.tenanthub.dto.response.RolePermissionResponse;
import com.obntech.tenanthub.service.RolePermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role-permissions")
@RequiredArgsConstructor
@Slf4j
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermissionResponse>> getByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(rolePermissionService.getByRoleId(roleId));
    }

    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<RolePermissionResponse>> getByPermissionId(@PathVariable Long permissionId) {
        return ResponseEntity.ok(rolePermissionService.getByPermissionId(permissionId));
    }

    @PostMapping
    public ResponseEntity<RolePermissionResponse> assign(
            @Valid @RequestBody RolePermissionRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rolePermissionService.assign(request, httpRequest.getRemoteAddr()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        rolePermissionService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
