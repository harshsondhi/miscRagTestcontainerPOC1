package com.sondhi.harsh.testcontainerpoc1.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldFundAllPosts(){
        Post[] posts = testRestTemplate.getForObject("/api/posts",Post[].class);
        assertThat(posts.length).isEqualTo(101++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++);
    }

    @Test
    void should_find_post_with_valid_post_id(){
        ResponseEntity<Post> response = testRestTemplate.exchange("/api/posts/1", HttpMethod.GET,null,Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

    }

    @Test
    void shouldThrowNotFoundWhenInvalidPostID() {
        ResponseEntity<Post> response = testRestTemplate.exchange("/api/posts/999", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Rollback
    void shouldCreateNewPostWhenPostIsValid() {
        Post post = new Post(101,1,"101 Title","101 Body",null);

        ResponseEntity<Post> response = testRestTemplate.exchange("/api/posts", HttpMethod.POST, new HttpEntity<Post>(post), Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(101);
        assertThat(response.getBody().userId()).isEqualTo(1);
        assertThat(response.getBody().title()).isEqualTo("101 Title");
        assertThat(response.getBody().body()).isEqualTo("101 Body");
    }

    @Test
    void shouldNotCreateNewPostWhenValidationFails() {
        Post post = new Post(101,1,"","",null);
        ResponseEntity<Post> response = testRestTemplate.exchange("/api/posts", HttpMethod.POST, new HttpEntity<Post>(post), Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdatePostWhenPostIsValid() {
        ResponseEntity<Post> response = testRestTemplate.exchange("/api/posts/99", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post existing = response.getBody();
        assertThat(existing).isNotNull();
        Post updated = new Post(existing.id(),existing.userId(),"NEW POST TITLE #1", "NEW POST BODY #1",existing.version());

        assertThat(updated.id()).isEqualTo(99);
        assertThat(updated.userId()).isEqualTo(10);
        assertThat(updated.title()).isEqualTo("NEW POST TITLE #1");
        assertThat(updated.body()).isEqualTo("NEW POST BODY #1");
    }

    @Test
    @Rollback
    void shouldDeleteWithValidID() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/posts/88", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}