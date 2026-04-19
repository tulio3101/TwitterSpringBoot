package edu.tdse.models.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;


@Document(collection = "streams")
@Data
@Builder
public class Stream {
    @Id
    private String streamId;

    private List<String> postsId;


}