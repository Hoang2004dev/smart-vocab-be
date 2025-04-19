package com.smartvocab.smart_vocab_backend.repository;

import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.entity.Vocabulary;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, UUID> {

}

