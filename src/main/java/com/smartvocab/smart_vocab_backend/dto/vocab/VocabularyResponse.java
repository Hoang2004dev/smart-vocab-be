package com.smartvocab.smart_vocab_backend.dto.vocab;

import com.smartvocab.smart_vocab_backend.entity.User;
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
public class VocabularyResponse {
        private UUID id;
        private String word;
        private String meaning;
        private String example;
        private String note;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
