package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.config.JwtProperties;
import com.obntech.tenanthub.dto.request.LoginRequest;
import com.obntech.tenanthub.dto.response.LoginResponse;
import com.obntech.tenanthub.dto.request.RefreshTokenRequest;
import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.repository.UserRepository;
import com.obntech.tenanthub.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Son giriş tarihini güncelle
        userRepository.findByUsername(request.getUsername()).ifPresent(userX -> {
            userX.setLastLoginDate(LocalDateTime.now());
            userX.setErrorLoginCount(0);
            userRepository.save(userX);
        });

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, jwtProperties.getAccessTokenExpiration()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            return ResponseEntity.badRequest().build();
        }

        String username = jwtTokenProvider.getUsernameFromToken(request.getRefreshToken());
        var userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String accessToken = jwtTokenProvider.generateAccessToken(authToken);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authToken);

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, jwtProperties.getAccessTokenExpiration()));
    }
}