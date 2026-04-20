package edu.tdse.models.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Data;

@Document(collection = "users")
@Data
@Builder
public class User {
   
    @Id
    private String id;
    private String name;
    private String email;

    private List<String> postsId;

    

}
