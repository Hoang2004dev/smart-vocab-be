package com.smartvocab.smart_vocab_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// AuthResponse.java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String accessToken;
    String refreshToken;
    String deviceId;
}
