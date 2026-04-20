package edu.tdse.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.tdse.exception.PostNotFoundException;
import edu.tdse.models.dto.request.PostRequestDTO;
import edu.tdse.models.dto.response.PostResponseDTO;
import edu.tdse.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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

        PostResponseDTO updatedPost = postService.updatePost(postId, post);

        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping()
    @Operation(
        summary = "Retrieve all posts",
        description = "Fetches all posts from the system. Returns a list of all available posts."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Posts retrieved successfully",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Post not found - no posts exist in the system",
            content = @Content(schema = @Schema(implementation = PostNotFoundException.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while retrieving posts"
        )
    })
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){

        return ResponseEntity.ok(postService.getAllPosts());

    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post", description = "Deletes a post and removes it from the stream.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found",
            content = @Content(schema = @Schema(implementation = PostNotFoundException.class)))
    })
    public ResponseEntity<Void> deletePost(
        @Parameter(description = "Unique identifier of the post to delete")
        @PathVariable String postId) {

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

}
