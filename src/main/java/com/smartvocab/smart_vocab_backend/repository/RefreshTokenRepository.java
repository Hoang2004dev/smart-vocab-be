package com.smartvocab.smart_vocab_backend.repository;

import com.smartvocab.smart_vocab_backend.entity.RefreshToken;
import com.smartvocab.smart_vocab_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    List<RefreshToken> findAllByUser(User user);
    void deleteAllByUser(User user);
    Optional<RefreshToken> findByUserAndDeviceIdAndIsRevokedFalse(User user, String deviceId);
    List<RefreshToken> findByUserAndIsRevoked(User user, boolean isRevoked);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.isRevoked = true WHERE r.user.id = :userId AND r.isRevoked = false")
    void revokeAllTokensByUser(@Param("userId") UUID userId);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndDeviceId(String token, String deviceId);

    Optional<RefreshToken> findByTokenAndDeviceIdAndIsRevokedFalseAndExpiresAtAfter(
            String token, String deviceId, LocalDateTime dateTime);

    List<RefreshToken> findByUser(User user);

    List<RefreshToken> findByUserAndIsRevokedFalseAndExpiresAtAfter(
            User user, LocalDateTime dateTime);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiresAt < :date")
    void deleteByExpiresAtBefore(LocalDateTime date);
}

