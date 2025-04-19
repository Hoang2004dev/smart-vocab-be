package com.smartvocab.smart_vocab_backend.dto.vocab;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveVocabsFromCollectionRequest {
    @NotEmpty
    private List<UUID> vocabIds;
}
