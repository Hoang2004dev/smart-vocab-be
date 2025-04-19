package com.smartvocab.smart_vocab_backend.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TagResponse {
    private UUID id;

    private String name;

    private String createdAt;
}
