package com.smartvocab.smart_vocab_backend.service;

import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.CollectionTag;
import com.smartvocab.smart_vocab_backend.entity.Tag;
import com.smartvocab.smart_vocab_backend.repository.CollectionRepository;
import com.smartvocab.smart_vocab_backend.repository.CollectionTagRepository;
import com.smartvocab.smart_vocab_backend.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CollectionTagService {
    private final CollectionRepository collectionRepository;
    private final TagRepository tagRepository;
    private final CollectionTagRepository collectionTagRepository;

    public void addTagsToCollection(UUID collectionId, Set<UUID> tagIds) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);

        for (Tag tag : tags) {
            CollectionTag.CollectionTagId id = new CollectionTag.CollectionTagId(collection.getId(), tag.getId());
            if (!collectionTagRepository.existsById(id)) {
                CollectionTag ct = new CollectionTag();
                ct.setId(id);
                ct.setCollection(collection);
                ct.setTag(tag);
                collectionTagRepository.save(ct);
            }
        }
    }

    public void removeTagsFromCollection(UUID collectionId, Set<UUID> tagIds) {
        for (UUID tagId : tagIds) {
            CollectionTag.CollectionTagId id = new CollectionTag.CollectionTagId(collectionId, tagId);
            if (collectionTagRepository.existsById(id)) {
                collectionTagRepository.deleteById(id);
            }
        }
    }
}

