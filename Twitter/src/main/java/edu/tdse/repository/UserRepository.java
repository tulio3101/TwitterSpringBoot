package edu.tdse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import edu.tdse.models.entity.User;

public interface UserRepository extends MongoRepository<User, String> {


}
