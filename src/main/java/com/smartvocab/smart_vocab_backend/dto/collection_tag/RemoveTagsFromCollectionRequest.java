package com.smartvocab.smart_vocab_backend.dto.collection_tag;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveTagsFromCollectionRequest {
    @NotEmpty
    private Set<UUID> tagIds;
}
