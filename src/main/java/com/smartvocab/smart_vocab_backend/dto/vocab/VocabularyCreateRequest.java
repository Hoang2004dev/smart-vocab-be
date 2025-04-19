package com.smartvocab.smart_vocab_backend.dto.vocab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyCreateRequest {
    private String word;
    private String meaning;
    private String example;
    private String note;
}
