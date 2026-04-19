package edu.tdse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;
import edu.tdse.exception.PostNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import edu.tdse.services.PostService;

@Tag(name = "Posts", description = "Post management operations - Create, update, and retrieve posts")
@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostController{

    private final PostService postService;

    @PostMapping("/create")
    @Operation(
        summary = "Create a new post",
        description = "Creates a new post with the provided content and metadata. Returns the created post with its ID and timestamp."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Post created successfully",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid post data provided - content may be empty or exceed maximum length"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while creating post"
        )
    })
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO post){

        PostResponseDTO created = postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
 
    }

    @PutMapping("/update/{postId}")
    @Operation(
        summary = "Update an existing post",
        description = "Updates a post with new content and metadata. The post must exist in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post updated successfully",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid post data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Post not found - the specified postId does not exist",
            content = @Content(schema = @Schema(implementation = PostNotFoundException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while updating post"
        )
    })
    public ResponseEntity<PostResponseDTO> updatePost(
        @Parameter(description = "Unique identifier of the post to update", example = "507f1f77bcf86cd799439011")
        @PathVariable String postId,
        @RequestBody PostRequestDTO post) {

        PostResponseDTO updatedPost = postService.updatePost(post);

        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping()
    @Operation(
        summary = "Retrieve a post by ID",
        description = "Fetches a specific post using its unique identifier. Returns the complete post data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Post not found - the specified postId does not exist",
            content = @Content(schema = @Schema(implementation = PostNotFoundException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while retrieving post"
        )
    })
    public ResponseEntity<PostResponseDTO> getPostById(
        @Parameter(description = "Unique identifier of the post to retrieve", example = "507f1f77bcf86cd799439011")
        @PathVariable String postId){

        return ResponseEntity.ok(postService.getPostById(postId));

    }

}