package com.obntech.tenanthub.controller;

import com.obntech.tenanthub.config.JwtProperties;
import com.obntech.tenanthub.dto.request.LoginRequest;
import com.obntech.tenanthub.dto.response.LoginResponse;
import com.obntech.tenanthub.repository.UserRepository;
import com.obntech.tenanthub.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final String REFRESH_TOKEN_PATH = "/api/v1/auth";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        userRepository.findByUsername(request.getUsername()).ifPresent(userX -> {
            userX.setLastLoginDate(LocalDateTime.now());
            userX.setErrorLoginCount(0);
            userRepository.save(userX);
        });

        ResponseCookie cookie = createRefreshTokenCookie(refreshToken, jwtProperties.getRefreshTokenExpiration() / 1000);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new LoginResponse(accessToken, jwtProperties.getAccessTokenExpiration()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().build();
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtTokenProvider.generateAccessToken(authToken);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authToken);

        ResponseCookie cookie = createRefreshTokenCookie(newRefreshToken, jwtProperties.getRefreshTokenExpiration() / 1000);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new LoginResponse(newAccessToken, jwtProperties.getAccessTokenExpiration()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = createRefreshTokenCookie("", 0);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private ResponseCookie createRefreshTokenCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, value)
            .httpOnly(true)
            .secure(true)
            .path(REFRESH_TOKEN_PATH)
            .maxAge(maxAgeSeconds)
            .sameSite("Strict")
            .build();
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
            .filter(c -> REFRESH_TOKEN_COOKIE.equals(c.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);
    }
}
