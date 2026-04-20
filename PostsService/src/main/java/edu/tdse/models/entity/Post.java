package edu.tdse.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Builder;


@Document(collection = "posts")
@Data
@Builder
public class Post {

    @Id
    private String postId;
 
    private String message;

    private String userId;

    private String userEmail;

    private String streamId;

}
