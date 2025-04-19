package com.smartvocab.smart_vocab_backend.dto.user;

import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.RefreshToken;
import com.smartvocab.smart_vocab_backend.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private UUID id;

    private String username;

    private String email;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
