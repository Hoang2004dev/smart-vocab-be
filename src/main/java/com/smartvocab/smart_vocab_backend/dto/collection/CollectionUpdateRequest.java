package com.smartvocab.smart_vocab_backend.dto.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionUpdateRequest {
    private String name;

    private String description;

    @Pattern(regexp = "private|public_all|public_specific_users")
    private String visibility;
}

