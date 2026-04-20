package edu.tdse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tdse.exception.StreamNotFoundException;
import edu.tdse.mapper.StreamMapper;
import edu.tdse.models.dto.response.StreamResponseDTO;
import edu.tdse.models.entity.Stream;
import edu.tdse.services.StreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Stream", description = "Stream management operations - Retrieve and manage user feed/stream")
@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class StreamController {
   
    private final StreamService streamService;
    private final StreamMapper streamMapper;


    @GetMapping
    @Operation(
        summary = "Get user stream",
        description = "Retrieves the complete user stream/feed containing all posts. Returns paginated or complete list depending on configuration."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stream retrieved successfully",
            content = @Content(schema = @Schema(implementation = StreamResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Stream not found - user stream does not exist",
            content = @Content(schema = @Schema(implementation = StreamNotFoundException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while retrieving stream"
        )
    })
    public ResponseEntity<StreamResponseDTO> getStream() {
        Stream stream = streamService.getStream();
        return ResponseEntity.ok(streamMapper.toDto(stream));
    }

    @DeleteMapping("/{postId}")
    @Operation(
        summary = "Remove post from stream",
        description = "Removes a specific post from the user's stream. The post must exist in the stream to be removed."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post removed from stream successfully",
            content = @Content(schema = @Schema(implementation = StreamResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Post or stream not found - either the postId does not exist or the stream is empty",
            content = @Content(schema = @Schema(implementation = StreamNotFoundException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while removing post from stream"
        )
    })
    public ResponseEntity<StreamResponseDTO> removePostFromStream(
        @Parameter(description = "Unique identifier of the post to remove from stream", example = "507f1f77bcf86cd799439011")
        @PathVariable String postId) {
        Stream stream = streamService.removePostFromStream(postId);

        return ResponseEntity.ok(streamMapper.toDto(stream));
    }

}