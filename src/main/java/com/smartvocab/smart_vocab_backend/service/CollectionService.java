package com.smartvocab.smart_vocab_backend.service;

import com.smartvocab.smart_vocab_backend.dto.collection.CollectionBasicInfo;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionResponse;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionUpdateRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyCreateRequest;
import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.entity.Vocabulary;
import com.smartvocab.smart_vocab_backend.mapper.CollectionMapper;
import com.smartvocab.smart_vocab_backend.repository.CollectionRepository;
import com.smartvocab.smart_vocab_backend.repository.VocabularyRepository;
import com.smartvocab.smart_vocab_backend.security.AuthenticatedUserProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final VocabularyRepository vocabularyRepository;
    private final CollectionMapper collectionMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public CollectionBasicInfo createCollection(CollectionCreateRequest request) {
        Collection collection = collectionMapper.toEntity(request);

        User currentUser = authenticatedUserProvider.getCurrentUser();
        collection.setUser(currentUser);

        Collection savedCollection = collectionRepository.save(collection);
        return collectionMapper.toBasicInfo(savedCollection);
    }

    public CollectionBasicInfo updateCollection(UUID id, CollectionUpdateRequest request) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        collectionMapper.updateCollectionFromDto(request, collection);

        Collection savedCollection = collectionRepository.save(collection);

        return collectionMapper.toBasicInfo(savedCollection);
    }

    public void deleteCollection(UUID id) {
        if (!collectionRepository.existsById(id)) {
            throw new EntityNotFoundException("Collection not found");
        }
        collectionRepository.deleteById(id);
    }

    public CollectionResponse getCollectionById(UUID id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        return collectionMapper.toDto(collection);
    }

    public List<CollectionBasicInfo> getCollectionsByUserId(UUID userId) {
        List<Collection> collections = collectionRepository.findByUserId(userId);

        return collectionMapper.toBasicInfoList(collections);
    }

    public void addVocabsToCollection(UUID collectionId, List<VocabularyCreateRequest> vocabRequests) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        List<Vocabulary> vocabularies = vocabRequests.stream()
                .map(req -> {
                    Vocabulary vocab = new Vocabulary();
                    vocab.setWord(req.getWord());
                    vocab.setMeaning(req.getMeaning());
                    vocab.setExample(req.getExample());
                    vocab.setNote(req.getNote());
                    vocab.setCollection(collection);
                    return vocab;
                })
                .collect(Collectors.toList());

        vocabularyRepository.saveAll(vocabularies);
    }

    public void removeVocabsFromCollection(UUID collectionId, List<UUID> vocabIds) {
        // Xác nhận collection tồn tại
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        // Lọc các vocabulary thuộc về collection hiện tại và có trong vocabIds
        List<Vocabulary> toDelete = vocabularyRepository.findAllById(vocabIds).stream()
                .filter(vocab -> vocab.getCollection().getId().equals(collectionId))
                .collect(Collectors.toList());

        vocabularyRepository.deleteAll(toDelete);
    }
}
