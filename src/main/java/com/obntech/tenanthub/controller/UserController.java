package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.UserCreateRequest;
import com.obntech.tenanthub.dto.request.UserUpdateRequest;
import com.obntech.tenanthub.dto.response.UserResponse;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.service.UserService;
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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<UserResponse>> getAllByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllByStatus(status, pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody UserCreateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request, httpRequest.getRemoteAddr()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(userService.update(id, request, httpRequest.getRemoteAddr()));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @PathVariable Status status,
            HttpServletRequest httpRequest) {
        userService.changeStatus(id, status, httpRequest.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
