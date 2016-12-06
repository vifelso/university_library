package com.university.library.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.university.library.model.Book;

@RepositoryRestResource(collectionResourceRel = "Book", path = "book")
public interface BookRepository extends MongoRepository<Book, String> {

	@Query("{$or: [{'Title' : {$regex : ?0}}, {'Publisher' : {$regex : ?0}}, {'Description' : {$regex : ?0}}, {'Authors' : {$regex : ?0}}]}")
	List<Book> findBooks(@Param("search") String searchString);
}
