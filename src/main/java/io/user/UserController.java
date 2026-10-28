package io.user;


import io.common.response.ApiResponse;
import io.user.dto.EmailRequest;
import io.user.dto.UserRequest;
import io.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
//    creating users
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request
            ){
        UserResponse user = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("User created successfully", user)
        );

    }
//    get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully", users)
        );
    }

//    get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ){
        UserResponse user = userService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(
                "User retrieved successfully", user
        )
        );
    }
//
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<UserResponse>> findByEmail(
            @Valid @RequestBody EmailRequest request
            )
    {
        UserResponse response = userService.findByEmail(request.email());

        return ResponseEntity.ok(ApiResponse.success(
                "User found", response
        ));
    }

}

