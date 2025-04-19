package com.smartvocab.smart_vocab_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "collection_tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionTag {
    @EmbeddedId
    private CollectionTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Data
    @Embeddable
    public static class CollectionTagId implements Serializable {

        @Column(name = "collection_id")
        private UUID collectionId;

        @Column(name = "tag_id")
        private UUID tagId;

        public CollectionTagId() {
        }

        public CollectionTagId(UUID collectionId, UUID tagId) {
            this.collectionId = collectionId;
            this.tagId = tagId;
        }
    }
}