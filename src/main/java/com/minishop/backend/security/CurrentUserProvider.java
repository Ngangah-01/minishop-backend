package com.minishop.backend.security;

import com.minishop.backend.user.User;
import com.minishop.backend.user.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//**********************//

@Component
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Extracts the authenticated user's email/username.
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user present in security context");
        }

        return authentication.getName();
    }

    /**
     * Extracts the authenticated domain User entity directly from the Principal
     * or queries the database as a fallback.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user present in security context");
        }

//        // 1. Direct Principal Extraction (0 extra DB queries if User implements UserDetails)
//        if (authentication.getPrincipal() instanceof User user) {
//            return user;
//        }

        // 2. Fallback DB lookup if principal holds only email/UserDetails wrapper
        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database: " + email));
    }
}