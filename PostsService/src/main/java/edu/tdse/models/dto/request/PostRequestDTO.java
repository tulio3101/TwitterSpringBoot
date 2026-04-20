package edu.tdse.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request data model for creating or updating a post")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    @Schema(
        description = "The text content of the post",
        example = "Hello Twitter! This is my first post.",
        minLength = 1,
        maxLength = 280
    )
    private String message;

    @Schema(
        description = "Unique identifier of the user who created the post",
        example = "507f1f77bcf86cd799439012"
    )
    private String userId;

    @Schema(description = "Email of the user who created the post")
    private String userEmail;

    @Schema(
        description = "Unique identifier of the stream where the post belongs",
        example = "507f1f77bcf86cd799439013"
    )
    private String streamId;

}
