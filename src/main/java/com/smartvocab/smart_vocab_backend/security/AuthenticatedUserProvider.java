package com.smartvocab.smart_vocab_backend.security;

import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.repository.UserRepository;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticatedUserProvider {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Cannot get current user");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // This holds the user ID

        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
}

