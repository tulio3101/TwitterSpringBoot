package edu.tdse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.tdse.models.entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    
}
