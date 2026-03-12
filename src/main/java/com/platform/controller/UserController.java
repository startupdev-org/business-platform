package com.platform.controller;

import com.platform.dto.auth.WhoAmIResponseDTO;
import com.platform.dto.user.UserRequestDTO;
import com.platform.dto.user.UserResponseDTO;
import com.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User", description = "User management endpoints")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Who am I", description = "Returns the currently authenticated user's information")
    @ApiResponse(responseCode = "200", description = "Authenticated user retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated")
    @GetMapping("/whoami")
    public ResponseEntity<WhoAmIResponseDTO> whoami() {
        return ResponseEntity.ok(userService.whoami());
    }

    @Operation(summary = "List users", description = "Returns a paginated list of all users. Accessible by admins only")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Not authorized to list users")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDTO> users = userService.listUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Returns a single user by their UUID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Not authorized to view this user")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User UUID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        UserResponseDTO user = userService.getUserDTOById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Not authorized to update this user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User UUID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID. Accessible by admins or the user themselves")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "403", description = "Not authorized to delete this user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User UUID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}