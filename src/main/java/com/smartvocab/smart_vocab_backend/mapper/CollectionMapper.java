package com.smartvocab.smart_vocab_backend.mapper;

import com.smartvocab.smart_vocab_backend.dto.collection.CollectionBasicInfo;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionCreateRequest;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionResponse;
import com.smartvocab.smart_vocab_backend.dto.collection.CollectionUpdateRequest;
import com.smartvocab.smart_vocab_backend.dto.tag.TagResponse;
import com.smartvocab.smart_vocab_backend.dto.vocab.VocabularyResponse;
import com.smartvocab.smart_vocab_backend.entity.Collection;
import com.smartvocab.smart_vocab_backend.entity.CollectionTag;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        UserMapper.class,
        VocabularyMapper.class,
        TagMapper.class
})
public interface CollectionMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(target = "vocabularies", expression = "java(mapVocabularies(collection.getCollectionVocabularies()))")
    @Mapping(target = "tags", expression = "java(mapTags(collection.getCollectionTags()))")
    CollectionResponse toDto(Collection collection);

    // Custom mapping methods
    default Set<VocabularyResponse> mapVocabularies(Set<CollectionVocabulary> collectionVocabularies) {
        if (collectionVocabularies == null) return null;
        return collectionVocabularies.stream()
                .map(cv -> VocabularyMapper.INSTANCE.toDto(cv.getVocabulary()))
                .collect(Collectors.toSet());
    }

    default Set<TagResponse> mapTags(Set<CollectionTag> collectionTags) {
        if (collectionTags == null) return null;
        return collectionTags.stream()
                .map(ct -> TagMapper.INSTANCE.toDto(ct.getTag()))
                .collect(Collectors.toSet());
    }

    CollectionBasicInfo toBasicInfo(Collection collection);

    List<CollectionBasicInfo> toBasicInfoList(List<Collection> collections);

    // Map DTO -> entity
    Collection toEntity(CollectionCreateRequest request);

    // Cập nhật entity từ DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCollectionFromDto(CollectionUpdateRequest request, @MappingTarget Collection collection);
}
