package com.smartvocab.smart_vocab_backend.repository;

import com.smartvocab.smart_vocab_backend.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    List<Collection> findByUserId(UUID userId);

    List<Collection> findByVisibility(String visibility);

    Optional<Collection> findByIdAndUserId(UUID id, UUID userId);
}

