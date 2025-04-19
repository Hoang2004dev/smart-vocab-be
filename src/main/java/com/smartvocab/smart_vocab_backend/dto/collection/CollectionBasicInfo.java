package com.smartvocab.smart_vocab_backend.dto.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionBasicInfo {
    private UUID id;

    private String name;

    private String description;

    private String visibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
