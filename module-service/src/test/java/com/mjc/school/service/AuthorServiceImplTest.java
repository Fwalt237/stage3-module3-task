package com.mjc.school.service;

import com.mjc.school.repository.impl.AuthorRepositoryImpl;
import com.mjc.school.repository.impl.NewsRepositoryImpl;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthorServiceImplTest {

     private AuthorServiceImpl authorServiceImpl;
     private AuthorRepositoryImpl authorRepositoryImpl;
     private NewsRepositoryImpl newsRepositoryImpl;

    private Author testAuthor;
    private News testNews;

    @BeforeEach
    @Transactional
    void setUp() {
        testAuthor = new Author();
        testAuthor.setName("Jane Doe");
        testAuthor = authorRepositoryImpl.create(testAuthor);

        testNews = new News();
        testNews.setTitle("Test News");
        testNews.setContent("Content");
        testNews.setAuthor(testAuthor);
        testNews = newsRepositoryImpl.create(testNews);
    }

    @Test
    void testReadAll() {
        List<AuthorDtoResponse> result = authorServiceImpl.readAll();
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(dto -> dto.name().equals("Jane Doe"));
    }

    @Test
    void testReadById() {
        AuthorDtoResponse result = authorServiceImpl.readById(testAuthor.getId());
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Doe");
    }

    @Test
    void testCreate() {
        AuthorDtoRequest request = new AuthorDtoRequest(null, "John Smith");
        AuthorDtoResponse result = authorServiceImpl.create(request);
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("John Smith");
        assertThat(authorRepositoryImpl.readById(result.authorId())).isPresent();
    }

    @Test
    void testUpdate() {
        AuthorDtoRequest request = new AuthorDtoRequest(testAuthor.getId(), "Jane Updated");
        AuthorDtoResponse result = authorServiceImpl.update(request);
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Updated");
    }

    @Test
    void testDeleteById() {
        boolean deleted = authorServiceImpl.deleteById(testAuthor.getId());
        assertThat(deleted).isTrue();
        assertThat(authorRepositoryImpl.readById(testAuthor.getId())).isEmpty();
    }

    @Test
    void testFindAuthorByNewsId() {
        AuthorDtoResponse result = authorServiceImpl.findAuthorByNewsId(testNews.getId());
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Doe");
    }

    @Test
    void testFindAuthorByNewsIdNotFound() {
        assertThrows(NotFoundException.class, () -> authorServiceImpl.findAuthorByNewsId(999L));
    }
}