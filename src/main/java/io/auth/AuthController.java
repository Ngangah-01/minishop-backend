package io.auth;

import io.auth.dto.AuthResponse;
import io.auth.dto.LoginRequest;
import io.auth.dto.RegisterRequest;
import io.common.response.ApiResponse;
import io.security.CurrentUserProvider;
import io.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserProvider currentUserProvider;

    public AuthController(AuthService authService, CurrentUserProvider currentUserProvider) {
        this.authService = authService;
        this.currentUserProvider = currentUserProvider;
    }

//    register user
@PostMapping("/register")
public ResponseEntity<ApiResponse<AuthResponse>> register(
        @Valid @RequestBody RegisterRequest request
) {
    AuthResponse response = authService.register(request);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    ApiResponse.success(
                            "Registration successful",
                            response
                    )
            );
}

    //login user
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(
            @Valid @RequestBody LoginRequest request
    ){
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User logged in successfully",
                        authResponse)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse user = UserResponse.from(currentUserProvider.getCurrentUser());
        return ResponseEntity.ok(
                ApiResponse.success("Current user has been retrieved successfully", user)
        );
    }
}
