package com.smartvocab.smart_vocab_backend.controller;

import com.smartvocab.smart_vocab_backend.dto.auth.*;
import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request,
                              @RequestHeader(value = "Device-Id", required = false) String deviceId,
                              @RequestHeader("User-Agent") String userAgent) {
        return authService.login(request, deviceId, userAgent);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request,
                                     @RequestHeader("Device-Id") String deviceId) {
        return authService.refreshToken(request, deviceId);
    }

    @PostMapping("/refresh-access-token")
    public AuthResponse refreshAccessToken(@RequestBody RefreshAccessTokenRequest request,
                                           @RequestHeader("Device-Id") String deviceId) {
        return authService.refreshAccessToken(request, deviceId);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request,
                       @RequestHeader("Device-Id") String deviceId) {
        authService.logout(request, deviceId);
    }

    @GetMapping("/devices")
    public List<DeviceInfo> getUserDevices(@AuthenticationPrincipal User user) {
        return authService.getUserDevices(user);
    }

    @PostMapping("/revoke-all")
    public ResponseEntity<Void> revokeAllTokens(@RequestParam UUID userId) {
        authService.revokeAllRefreshTokens(userId);
        return ResponseEntity.ok().build();
    }

    // hi
}


