package com.university.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.university.library.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByUsername(String username);
}