//package com.minishop.backend.security;
//
//import com.minishop.backend.user.Role;
//import com.minishop.backend.user.User;
//import com.minishop.backend.user.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AdminSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) {
//        String email = "admin@minishop.com";
//        if (userRepository.existsByEmailIgnoreCase(email)) return;
//        userRepository.save(new User("Admin", email, passwordEncoder.encode("admin123"), Role.ADMIN));
//    }
//}