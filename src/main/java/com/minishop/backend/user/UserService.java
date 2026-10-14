package com.minishop.backend.user;

import com.minishop.backend.common.exception.ResourceNotFoundException;
import com.minishop.backend.user.dto.UserRequest;
import com.minishop.backend.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmailIgnoreCase(userRequest.email())) {
            throw new IllegalArgumentException("User with email *" + userRequest.email() + "* already exists");
        }

        User user = new User(
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                passwordEncoder.encode(userRequest.password()),
                Role.CUSTOMER
        );

        return UserResponse.from(userRepository.save(user));
    }
//    find all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
//    find by id
    public UserResponse findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"
                        )
                );
        return UserResponse.from(user);
    }
//    find by email
    public UserResponse findByEmail(String email){
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User with email " + email + " not found"
                        )
                );
        return UserResponse.from(user);
    }
}
