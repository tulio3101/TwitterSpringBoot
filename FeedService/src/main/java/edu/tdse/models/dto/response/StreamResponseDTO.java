package edu.tdse.models.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response model containing user stream/feed information with list of posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamResponseDTO {
   
    @Schema(
        description = "Unique identifier of the stream",
        example = "507f1f77bcf86cd799439013"
    )
    private String streamId;

    @Schema(
        description = "List of unique identifiers of posts in the stream",
        example = "[\"507f1f77bcf86cd799439011\", \"507f1f77bcf86cd799439014\", \"507f1f77bcf86cd799439015\"]"
    )
    private List<String> postsId;

}
