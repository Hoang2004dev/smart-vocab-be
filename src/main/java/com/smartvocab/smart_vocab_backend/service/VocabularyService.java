package com.smartvocab.smart_vocab_backend.service;

import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyResponse;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.entity.Vocabulary;
import com.smartvocab.smart_vocab_backend.mapper.VocabularyMapper;
import com.smartvocab.smart_vocab_backend.repository.CollectionRepository;
import com.smartvocab.smart_vocab_backend.repository.VocabularyRepository;
import com.smartvocab.smart_vocab_backend.security.AuthenticatedUserProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VocabularyService {
    private final VocabularyMapper vocabularyMapper;
    private final VocabularyRepository vocabularyRepository;
    private final CollectionRepository collectionRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public VocabularyResponse createVocabulary(UUID collectionId, VocabularyCreateRequest request) {
        Collection collection = collectionRepository.findById(collectionId).
                orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        Vocabulary vocabulary = vocabularyMapper.toEntity(request);
        vocabulary.setCollection(collection);

        Vocabulary savedVocab = vocabularyRepository.save(vocabulary);
        return vocabularyMapper.toDto(savedVocab);
    }

    public VocabularyResponse getVocabulary(UUID id) {
        Vocabulary vocab = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        return vocabularyMapper.toDto(vocab);
    }

    public VocabularyResponse updateVocabulary(UUID id, VocabularyUpdateRequest request) {
        Vocabulary vocab = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        vocabularyMapper.updateEntityFromDto(request, vocab);

        Vocabulary savedVocab = vocabularyRepository.save(vocab);

        return vocabularyMapper.toDto(savedVocab);
    }


    public void deleteVocabulary(UUID id) {
        Vocabulary vocab = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));
        vocabularyRepository.delete(vocab);
    }

//    public List<VocabularyResponse> getUserVocabularies(UUID userId) {
//        List<Vocabulary> vocabList = vocabularyRepository.findByUserId(userId);
//        return vocabList.stream()
//                .map(vocabularyMapper::toResponse)
//                .collect(Collectors.toList());
//    }

}

