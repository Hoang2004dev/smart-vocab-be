package com.smartvocab.smart_vocab_backend.repository;

import com.smartvocab.smart_vocab_backend.entity.CollectionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionTagRepository extends JpaRepository<CollectionTag, CollectionTag.CollectionTagId> {
}

