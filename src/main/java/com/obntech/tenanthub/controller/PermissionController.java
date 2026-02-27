package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.PermissionCreateRequest;
import com.obntech.tenanthub.dto.request.PermissionUpdateRequest;
import com.obntech.tenanthub.dto.response.PermissionResponse;
import com.obntech.tenanthub.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PermissionResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(permissionService.getAll(pageable));
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<List<PermissionResponse>> getByModule(@PathVariable String module) {
        return ResponseEntity.ok(permissionService.getByModule(module));
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> create(
            @Valid @RequestBody PermissionCreateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.create(request, httpRequest.getRemoteAddr()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(permissionService.update(id, request, httpRequest.getRemoteAddr()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
