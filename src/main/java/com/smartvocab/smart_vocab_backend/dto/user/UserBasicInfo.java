package com.smartvocab.smart_vocab_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserBasicInfo {
    private String username;

    private String email;
}
