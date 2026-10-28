package io.auth;

import io.aspect.LogExecutionTime;
import io.auth.dto.AuthResponse;
import io.auth.dto.LoginRequest;
import io.auth.dto.RegisterRequest;
import io.security.CustomUserDetailsService;
import io.security.JwtService;
import io.user.Role;
import io.user.User;
import io.user.UserRepository;
import io.user.dto.UserResponse;
//import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
//    private final MessageSource messageSource;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService
//            MessageSource messageSource
    ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
//        this.messageSource = messageSource;
    }

    @LogExecutionTime
    public AuthResponse register(RegisterRequest request) {

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.email(),
                encoder.encode(request.password()),
                Role.CUSTOMER
        );
        User savedUser = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, UserResponse.from(savedUser));
    }

//    ************ //
public AuthResponse login(LoginRequest request) {

    try {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        assert userDetails != null;
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, UserResponse.from(user));

    } catch (AuthenticationException e) {
        throw new BadCredentialsException(
                "Invalid email or password"
        );
    }
}
}
