
package edu.tdse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UserController{

    private final UserService userService;

    @GetMapping("/api/{id}")
    @Operation(
        summary = "Get user personal information",
        description = "Retrieves detailed profile information for a specific user including username, email, and account metadata."
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
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while retrieving user information"
        )
    })
    public ResponseEntity<UserResponseDTO> getPersonalInfo(
        @Parameter(description = "Unique identifier of the user to retrieve", example = "507f1f77bcf86cd799439012")
        @PathVariable String id){

        UserResponseDTO user = userService.getPersonalInfo(id);

        return ResponseEntity.ok(user);


    }

}