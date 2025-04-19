package com.smartvocab.smart_vocab_backend.service;

import com.smartvocab.smart_vocab_backend.dto.auth.*;
import com.smartvocab.smart_vocab_backend.entity.RefreshToken;
import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.exception.*;
import com.smartvocab.smart_vocab_backend.repository.RefreshTokenRepository;
import com.smartvocab.smart_vocab_backend.repository.UserRepository;
import com.smartvocab.smart_vocab_backend.security.AuthenticatedUserProvider;
import com.smartvocab.smart_vocab_backend.security.JwtUtil;
import com.smartvocab.smart_vocab_backend.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final RefreshTokenRepository tokenRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    private static final int MAX_REFRESH_TOKENS = 5;

    public void register(RegisterRequest request) {
        // Check if email already exists
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(encoder.encode(request.getPassword()))
                //.enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepo.save(user);
    }

    @Transactional
    public AuthResponse login(AuthRequest request, String deviceId, String userAgent) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            cleanupRefreshTokens(user);

            return generateTokens(user, deviceId, userAgent);
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request, String deviceId) {
        RefreshToken token = tokenRepo.findByTokenAndDeviceIdAndIsRevokedFalseAndExpiresAtAfter(
                        request.getRefreshToken(), deviceId, LocalDateTime.now())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid or expired refresh token"));

        // Revoke old token & generate new one
        token.setIsRevoked(true);
        tokenRepo.save(token);

        return generateTokens(token.getUser(), token.getDeviceId(), token.getUserAgent());
    }

    public AuthResponse refreshAccessToken(RefreshAccessTokenRequest request, String deviceId) {
        RefreshToken token = tokenRepo.findByTokenAndDeviceIdAndIsRevokedFalseAndExpiresAtAfter(
                        request.getRefreshToken(), deviceId, LocalDateTime.now())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid or expired refresh token"));

        String accessToken = jwtUtil.generateAccessToken(token.getUser().getId().toString(), token.getUser().getEmail());
        return new AuthResponse(accessToken, token.getToken(), deviceId);
    }

    public void logout(LogoutRequest request, String deviceId) {
        RefreshToken token = tokenRepo.findByTokenAndDeviceId(request.getRefreshToken(), deviceId)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token or device"));

        token.setIsRevoked(true);
        tokenRepo.save(token);
    }

    private AuthResponse generateTokens(User user, String deviceId, String userAgent) {
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getEmail());
        //String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString(), user.getEmail());
        String refreshToken = UUID.randomUUID().toString();

        // Normalize device ID
        deviceId = StringUtils.hasText(deviceId) ? deviceId : UUID.randomUUID().toString();

        RefreshToken rt = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .deviceId(deviceId)
                .userAgent(userAgent)
                .createdAt(LocalDateTime.now())
                .isRevoked(false)
                .build();
        tokenRepo.save(rt);

        return new AuthResponse(accessToken, refreshToken, rt.getDeviceId());
    }

    @Transactional
    protected void cleanupRefreshTokens(User user) {
        // Get all active tokens for user
        List<RefreshToken> activeTokens = tokenRepo.findByUserAndIsRevokedFalseAndExpiresAtAfter(
                user, LocalDateTime.now());

        // If limit reached, revoke oldest tokens
        if (activeTokens.size() >= MAX_REFRESH_TOKENS) {
            activeTokens.stream()
                    .sorted(Comparator.comparing(RefreshToken::getCreatedAt))
                    .limit(activeTokens.size() - MAX_REFRESH_TOKENS + 1)
                    .forEach(token -> {
                        token.setIsRevoked(true);
                        tokenRepo.save(token);
                    });
        }

        // Cleanup expired tokens in background (can be moved to a scheduled task)
        tokenRepo.deleteByExpiresAtBefore(LocalDateTime.now().minusDays(30));
    }

    @Transactional
    public void revokeAllRefreshTokens(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        tokenRepo.revokeAllTokensByUser(userId);
    }

    public List<DeviceInfo> getUserDevices(User user) {
        return tokenRepo.findByUserAndIsRevokedFalseAndExpiresAtAfter(user, LocalDateTime.now())
                .stream()
                .map(rt -> new DeviceInfo(rt.getDeviceId(), rt.getUserAgent(), rt.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
