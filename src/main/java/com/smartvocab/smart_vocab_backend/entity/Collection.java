package com.smartvocab.smart_vocab_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "collections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "visibility", nullable = false)
    private String visibility = "private";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vocabulary> vocabularies = new ArrayList<>();

    @OneToMany(mappedBy = "collection")
    private Set<CollectionUserProgress> collectionUserProgress;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionTag> collectionTags;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionSharedUser> collectionSharedUsers;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionAccessLog> accessLogs;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}