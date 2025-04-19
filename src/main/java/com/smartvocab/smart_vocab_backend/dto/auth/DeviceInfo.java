package com.smartvocab.smart_vocab_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {
    private String deviceId;
    private String userAgent;
    private LocalDateTime loginTime;
}

