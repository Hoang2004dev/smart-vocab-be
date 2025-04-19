package com.smartvocab.smart_vocab_backend.mapper;

import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyRequest;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyResponse;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Vocabulary;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VocabularyMapper {
    // Trick để dùng trong custom method
    VocabularyMapper INSTANCE = Mappers.getMapper(VocabularyMapper.class);

    VocabularyResponse toDto(Vocabulary vocabulary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VocabularyUpdateRequest dto, @MappingTarget Vocabulary entity);

    Vocabulary toEntity(VocabularyCreateRequest dto);
}

