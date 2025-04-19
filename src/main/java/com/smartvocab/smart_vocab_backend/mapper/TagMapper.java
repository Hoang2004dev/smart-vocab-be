package com.smartvocab.smart_vocab_backend.mapper;

import com.smartvocab.smart_vocab_backend.dto.collection.CollectionUpdateRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagResponse;
import com.smartvocab.smart_vocab_backend.dto.tag.TagUpdateRequest;
import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.Tag;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagResponse toDto(Tag tag);

    List<TagResponse> toDtoList(List<Tag> tags);

    Tag toEntity(TagCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTagFromDto(TagUpdateRequest request, @MappingTarget Tag tag);
}
