package io.security;


import io.user.User;
import io.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override // what does override mean in java -- it means that this method is overriding a method from the parent class or interface, in this case, the UserDetailsService interface, is used to provide a custom implementation of the loadUserByUsername method.
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException { // using the email as a username to load the user details from the database, and if the user is not found, it throws a RuntimeException with a message indicating that the user was not found.
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No User with email " + email)
                );
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
//                .roles(user.getRole().name())
                .build();
    }
}
