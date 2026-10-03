package com.minishop.backend.auth;

import com.minishop.backend.auth.dto.AuthResponse;
import com.minishop.backend.auth.dto.AuthResponse;
import com.minishop.backend.auth.dto.LoginRequest;
import com.minishop.backend.auth.dto.RegisterRequest;
import com.minishop.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
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

}
