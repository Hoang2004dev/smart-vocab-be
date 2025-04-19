package com.smartvocab.smart_vocab_backend.dto.collection;

import com.smartvocab.smart_vocab_backend.dto.tag.TagResponse;
import com.smartvocab.smart_vocab_backend.dto.user.UserBasicInfo;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyResponse;
import com.smartvocab.smart_vocab_backend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CollectionResponse {
    private UUID id;

    private UserBasicInfo user;

    private String name;

    private String description;

    private String visibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<VocabularyResponse> vocabularies;

    private Set<TagResponse> tags;
}
