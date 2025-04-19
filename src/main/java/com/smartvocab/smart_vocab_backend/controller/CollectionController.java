package com.smartvocab.smart_vocab_backend.controller;

import com.smartvocab.smart_vocab_backend.dto.collection.CollectionBasicInfo;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionResponse;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionUpdateRequest;
import com.smartvocab.smart_vocab_backend.dto.collection_tag.AddTagsToCollectionRequest;
import com.smartvocab.smart_vocab_backend.dto.collection_tag.RemoveTagsFromCollectionRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.AddVocabsToCollectionRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.RemoveVocabsFromCollectionRequest;
import com.smartvocab.smart_vocab_backend.service.CollectionService;
import com.smartvocab.smart_vocab_backend.service.CollectionTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;
    private final CollectionTagService collectionTagService;

    @PostMapping
    public ResponseEntity<CollectionBasicInfo> create(@RequestBody CollectionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionService.createCollection(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionBasicInfo> update(@PathVariable UUID id, @RequestBody CollectionUpdateRequest request) {
        return ResponseEntity.ok(collectionService.updateCollection(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(collectionService.getCollectionById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollectionBasicInfo>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(collectionService.getCollectionsByUserId(userId));
    }

    // ==============================================================

    @PostMapping("/{collectionId}/tags")
    public ResponseEntity<?> addTags(@PathVariable UUID collectionId,
                                     @RequestBody @Valid AddTagsToCollectionRequest request) {
        collectionTagService.addTagsToCollection(collectionId, request.getTagIds());
        return ResponseEntity.ok("Tags added to collection");
    }

    @DeleteMapping("/{collectionId}/tags")
    public ResponseEntity<?> removeTags(@PathVariable UUID collectionId,
                                        @RequestBody @Valid RemoveTagsFromCollectionRequest request) {
        collectionTagService.removeTagsFromCollection(collectionId, request.getTagIds());
        return ResponseEntity.ok("Tags removed from collection");
    }

    // ==============================================================

    @PostMapping("/{collectionId}/vocabularies")
    public ResponseEntity<?> addVocabs(@PathVariable UUID collectionId,
                                       @RequestBody @Valid AddVocabsToCollectionRequest request) {
        collectionService.addVocabsToCollection(collectionId, request.getVocabs());
        return ResponseEntity.ok("Vocabularies added to collection");
    }

    @DeleteMapping("/{collectionId}/vocabularies")
    public ResponseEntity<?> removeVocabs(@PathVariable UUID collectionId,
                                          @RequestBody @Valid RemoveVocabsFromCollectionRequest request) {
        collectionService.removeVocabsFromCollection(collectionId, request.getVocabIds());
        return ResponseEntity.ok("Vocabularies removed from collection");
    }
}
