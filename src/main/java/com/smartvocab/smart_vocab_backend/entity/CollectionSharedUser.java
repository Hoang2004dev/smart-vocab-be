package com.smartvocab.smart_vocab_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "collection_shared_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSharedUser {
    @EmbeddedId
    private CollectionSharedUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sharedUserId")
    @JoinColumn(name = "shared_user_id")
    private User user;

    @Column(name = "permission")
    private String permission = "read";

    @Data
    @Embeddable
    public static class CollectionSharedUserId implements Serializable {

        @Column(name = "collection_id")
        private UUID collectionId;

        @Column(name = "shared_user_id")
        private UUID sharedUserId;

        public CollectionSharedUserId() {
        }

        public CollectionSharedUserId(UUID collectionId, UUID sharedUserId) {
            this.collectionId = collectionId;
            this.sharedUserId = sharedUserId;
        }
    }
}
