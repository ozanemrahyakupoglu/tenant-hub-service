package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.RealEstateCreateRequest;
import com.obntech.tenanthub.dto.request.RealEstateUpdateRequest;
import com.obntech.tenanthub.dto.response.RealEstateResponse;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.service.RealEstateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/real-estates")
@RequiredArgsConstructor
@Slf4j
public class RealEstateController {

    private final RealEstateService realEstateService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('REAL_ESTATE_READ')")
    public ResponseEntity<RealEstateResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(realEstateService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('REAL_ESTATE_READ')")
    public ResponseEntity<Page<RealEstateResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(realEstateService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('REAL_ESTATE_READ')")
    public ResponseEntity<Page<RealEstateResponse>> getAllByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(realEstateService.getAllByStatus(status, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('REAL_ESTATE_CREATE')")
    public ResponseEntity<RealEstateResponse> create(
            @Valid @RequestBody RealEstateCreateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(realEstateService.create(request, httpRequest.getRemoteAddr()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('REAL_ESTATE_UPDATE')")
    public ResponseEntity<RealEstateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RealEstateUpdateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(realEstateService.update(id, request, httpRequest.getRemoteAddr()));
    }

    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('REAL_ESTATE_UPDATE')")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @PathVariable Status status,
            HttpServletRequest httpRequest) {
        realEstateService.changeStatus(id, status, httpRequest.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('REAL_ESTATE_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        realEstateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
