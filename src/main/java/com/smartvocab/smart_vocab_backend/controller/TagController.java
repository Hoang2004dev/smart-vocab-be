package com.smartvocab.smart_vocab_backend.controller;

import com.smartvocab.smart_vocab_backend.dto.tag.TagCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagResponse;
import com.smartvocab.smart_vocab_backend.dto.tag.TagUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Tag;
import com.smartvocab.smart_vocab_backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagCreateRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable UUID id) {
        return ResponseEntity.ok(tagService.getTag(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<TagResponse>> getUserTag() {
        return ResponseEntity.ok(tagService.getUserTags());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable UUID id, @RequestBody TagUpdateRequest request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }
}
