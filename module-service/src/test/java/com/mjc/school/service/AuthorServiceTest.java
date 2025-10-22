package com.mjc.school.service;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthorServiceTest {

     private AuthorService authorService ;
     private AuthorRepository authorRepository;
     private NewsRepository newsRepository;

    private Author testAuthor;
    private News testNews;

    @BeforeEach
    @Transactional
    void setUp() {
        testAuthor = new Author();
        testAuthor.setName("Jane Doe");
        testAuthor = authorRepository.create(testAuthor);

        testNews = new News();
        testNews.setTitle("Test News");
        testNews.setContent("Content");
        testNews.setAuthor(testAuthor);
        testNews = newsRepository.create(testNews);
    }

    @Test
    void testReadAll() {
        List<AuthorDtoResponse> result = authorService.readAll();
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(dto -> dto.name().equals("Jane Doe"));
    }

    @Test
    void testReadById() {
        AuthorDtoResponse result = authorService.readById(testAuthor.getId());
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Doe");
    }

    @Test
    void testCreate() {
        AuthorDtoRequest request = new AuthorDtoRequest(null, "John Smith");
        AuthorDtoResponse result = authorService.create(request);
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("John Smith");
        assertThat(authorRepository.readById(result.authorId())).isPresent();
    }

    @Test
    void testUpdate() {
        AuthorDtoRequest request = new AuthorDtoRequest(testAuthor.getId(), "Jane Updated");
        AuthorDtoResponse result = authorService.update(request);
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Updated");
    }

    @Test
    void testDeleteById() {
        boolean deleted = authorService.deleteById(testAuthor.getId());
        assertThat(deleted).isTrue();
        assertThat(authorRepository.readById(testAuthor.getId())).isEmpty();
    }

    @Test
    void testFindAuthorByNewsId() {
        AuthorDtoResponse result = authorService.findAuthorByNewsId(testNews.getId());
        assertThat(result).isNotNull();
        assertThat(result.authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.name()).isEqualTo("Jane Doe");
    }

    @Test
    void testFindAuthorByNewsIdNotFound() {
        assertThrows(NotFoundException.class, () -> authorService.findAuthorByNewsId(999L));
    }
}