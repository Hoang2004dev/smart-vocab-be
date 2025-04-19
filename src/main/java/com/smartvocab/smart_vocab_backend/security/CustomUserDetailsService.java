package com.smartvocab.smart_vocab_backend.security;

import com.smartvocab.smart_vocab_backend.entity.User;
import com.smartvocab.smart_vocab_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // UserDetails username is mapped to user ID for JWT validation
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),  // Username field holds the user ID
                user.getPasswordHash(),
                List.of()  // Empty authorities list
        );
    }
}
