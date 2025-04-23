package com.smartvocab.smart_vocab_backend.service;

import com.smartvocab.smart_vocab_backend.dto.tag.TagCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagResponse;
import com.smartvocab.smart_vocab_backend.dto.tag.TagUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Tag;
import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.mapper.TagMapper;
import com.smartvocab.smart_vocab_backend.repository.TagRepository;
import com.smartvocab.smart_vocab_backend.security.AuthenticatedUserProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public TagResponse createTag(TagCreateRequest request) {
        Tag tag = tagMapper.toEntity(request);

        User currentUser = authenticatedUserProvider.getCurrentUser();

        tag.setUser(currentUser);

        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toDto(savedTag);
    }

    public TagResponse getTag(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        return tagMapper.toDto(tag);
    }

    public TagResponse updateTag(UUID id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        tagMapper.updateTagFromDto(request, tag);

        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toDto(savedTag);
    }

    public void deleteTag(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found");
        }

        tagRepository.deleteById(id);
    }

    public void deleteTag2(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found");
        }

        tagRepository.deleteById(id);
    }

    public List<TagResponse> getUserTags() {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        List<Tag> tags = tagRepository.findByUserId(currentUser.getId());

        return tagMapper.toDtoList(tags);
    }

    public List<TagResponse> getAllTags() {
        return tagMapper.toDtoList(tagRepository.findAll());
    }
}

