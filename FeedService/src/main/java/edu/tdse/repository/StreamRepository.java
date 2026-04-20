package edu.tdse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.tdse.models.entity.Stream;

public interface StreamRepository extends MongoRepository<Stream, String> {
    
}
