package edu.tdse.models.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Document(collection = "posts")
@Builder
@Data
public class Post {

    @Id
    private String postId;
 
    private String message;

    private String userId;

    private String streamId;

}
