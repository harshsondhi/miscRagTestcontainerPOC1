package com.sondhi.harsh.testcontainerpoc1.post;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

    Optional<Post> findByTitle(String title);
}
