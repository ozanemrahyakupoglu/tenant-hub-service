package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.RentCreateRequest;
import com.obntech.tenanthub.dto.request.RentUpdateRequest;
import com.obntech.tenanthub.dto.response.RentResponse;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.service.RentService;
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
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
@Slf4j
public class RentController {

    private final RentService rentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RENT_READ')")
    public ResponseEntity<RentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RENT_READ')")
    public ResponseEntity<Page<RentResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(rentService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('RENT_READ')")
    public ResponseEntity<Page<RentResponse>> getAllByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(rentService.getAllByStatus(status, pageable));
    }

    @GetMapping("/real-estate/{realEstateId}")
    @PreAuthorize("hasAuthority('RENT_READ')")
    public ResponseEntity<Page<RentResponse>> getAllByRealEstateId(
            @PathVariable Long realEstateId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(rentService.getAllByRealEstateId(realEstateId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RENT_CREATE')")
    public ResponseEntity<RentResponse> create(
            @Valid @RequestBody RentCreateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentService.create(request, httpRequest.getRemoteAddr()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RENT_UPDATE')")
    public ResponseEntity<RentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RentUpdateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(rentService.update(id, request, httpRequest.getRemoteAddr()));
    }

    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('RENT_UPDATE')")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @PathVariable Status status,
            HttpServletRequest httpRequest) {
        rentService.changeStatus(id, status, httpRequest.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RENT_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
