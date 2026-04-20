
package edu.tdse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tdse.models.dto.request.UserRequestDTO;
import edu.tdse.models.dto.response.UserResponseDTO;
import edu.tdse.exception.UserNotFoundException;
import edu.tdse.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users", description = "User management operations - Retrieve user profile and information")
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/me")
    @Operation(
        summary = "Register or retrieve current user",
        description = "Creates the user in the database if they do not exist yet. Returns the user's profile data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered or already exists",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponseDTO> registerMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserRequestDTO dto) {

        String userId = jwt.getSubject();
        UserResponseDTO user = userService.registerUser(userId, dto.getName(), dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current user info",
        description = "Returns the profile of the authenticated user from the database."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User information retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = UserNotFoundException.class))
        )
    })
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        UserResponseDTO user = userService.getPersonalInfo(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user personal information",
        description = "Retrieves detailed profile information for a specific user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User information retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found - the specified user ID does not exist",
            content = @Content(schema = @Schema(implementation = UserNotFoundException.class))
        )
    })
    public ResponseEntity<UserResponseDTO> getPersonalInfo(
        @Parameter(description = "Unique identifier of the user", example = "auth0|abc123")
        @PathVariable String id) {

        UserResponseDTO user = userService.getPersonalInfo(id);
        return ResponseEntity.ok(user);
    }
}
