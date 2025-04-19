package com.smartvocab.smart_vocab_backend.controller;

import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyResponse;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Vocabulary;
import com.smartvocab.smart_vocab_backend.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {
    private final VocabularyService vocabularyService;

    @PostMapping("/collection/{collectionId}")
    public ResponseEntity<VocabularyResponse> createVocabulary(@PathVariable UUID collectionId,
                                                               @RequestBody VocabularyCreateRequest request) {
        return ResponseEntity.ok(vocabularyService.createVocabulary(collectionId ,request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VocabularyResponse> getVocabulary(@PathVariable UUID id) {
        return ResponseEntity.ok(vocabularyService.getVocabulary(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VocabularyResponse> updateVocabulary(@PathVariable UUID id, @RequestBody VocabularyUpdateRequest request) {
        return ResponseEntity.ok(vocabularyService.updateVocabulary(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVocabulary(@PathVariable UUID id) {
        vocabularyService.deleteVocabulary(id);
        return ResponseEntity.noContent().build();
    }
}

