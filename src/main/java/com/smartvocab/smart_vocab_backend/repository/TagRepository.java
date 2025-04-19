package com.smartvocab.smart_vocab_backend.repository;

import com.smartvocab.smart_vocab_backend.entity.Tag;
import com.smartvocab.smart_vocab_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByName(String name);
    List<Tag> findByUserId(UUID userId);
}
