package com.obntech.tenanthub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Slf4j
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Health check isteği alındı");
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "TenantHub",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
