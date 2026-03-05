package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.dto.request.PaymentCreateRequest;
import com.obntech.tenanthub.dto.request.PaymentUpdateRequest;
import com.obntech.tenanthub.dto.response.PaymentResponse;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.service.PaymentService;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public ResponseEntity<Page<PaymentResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public ResponseEntity<Page<PaymentResponse>> getAllByStatus(
            @PathVariable Status status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAllByStatus(status, pageable));
    }

    @GetMapping("/rent/{rentId}")
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public ResponseEntity<Page<PaymentResponse>> getAllByRentId(
            @PathVariable Long rentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAllByRentId(rentId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_CREATE')")
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody PaymentCreateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.create(request, httpRequest.getRemoteAddr()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_UPDATE')")
    public ResponseEntity<PaymentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentUpdateRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(paymentService.update(id, request, httpRequest.getRemoteAddr()));
    }

    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasAuthority('PAYMENT_UPDATE')")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @PathVariable Status status,
            HttpServletRequest httpRequest) {
        paymentService.changeStatus(id, status, httpRequest.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
