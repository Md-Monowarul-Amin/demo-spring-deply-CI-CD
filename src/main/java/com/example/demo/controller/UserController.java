package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Create user",
            description = "Create users"
    )
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(
            @Valid @RequestBody UserCreateRequestDTO requestDTO) {

        UserDTO userDTO = userService.createUser(requestDTO);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User created successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by Id",
            description = "Get user by Id"
    )
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update user",
            description = "Update a user"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO requestDTO) {

        UserDTO userDTO = userService.updateUser(id, requestDTO);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete User",
            description = "Delete a user"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .count(0)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Get all users"
    )
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> userPage = userService.getAllUsers(PageRequest.of(page, size));

        UserResponseDTO responseDTO = new UserResponseDTO(
                userPage.getContent(),
                userPage.getNumber(),
                userPage.getTotalPages(),
                userPage.getTotalElements()
        );

        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(responseDTO)
                .count(userPage.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }
}