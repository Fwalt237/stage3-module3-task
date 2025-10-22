package com.mjc.school.service;

import com.mjc.school.repository.impl.AuthorRepositoryImpl;
import com.mjc.school.repository.impl.NewsRepositoryImpl;
import com.mjc.school.repository.impl.TagRepositoryImpl;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NewsServiceTest {


    private NewsService newsService;
    private NewsRepositoryImpl newsRepositoryImpl;
    private AuthorRepositoryImpl authorRepositoryImpl;
    private TagRepositoryImpl tagRepositoryImpl;

    private Author testAuthor;
    private Tag testTag;
    private News testNews;

    @BeforeEach
    @Transactional
    void setUp() {
        testAuthor = new Author();
        testAuthor.setName("John Doe");
        testAuthor = authorRepositoryImpl.create(testAuthor);

        testTag = new Tag();
        testTag.setName("Tech");
        testTag = tagRepositoryImpl.create(testTag);

        testNews = new News();
        testNews.setTitle("Test News");
        testNews.setContent("Content");
        testNews.setAuthor(testAuthor);
        testNews.setTagsFromNews(Set.of(testTag));
        testNews = newsRepositoryImpl.create(testNews);
    }

    @Test
    void testReadAll() {
        List<NewsDtoResponse> result = newsService.readAll();
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(dto -> dto.title().equals("Test News"));
    }

    @Test
    void testReadById() {
        NewsDtoResponse result = newsService.readById(testNews.getId());
        assertThat(result).isNotNull();
        assertThat(result.newsId()).isEqualTo(testNews.getId());
        assertThat(result.title()).isEqualTo("Test News");
        assertThat(result.author().name()).isEqualTo("John Doe");
        assertThat(result.tags()).extracting(TagDtoResponse::name).contains("Tech");
    }

    @Test
    void testCreate() {
        NewsDtoRequest request = new NewsDtoRequest(null, "New News", "New Content",
                testAuthor.getId(), Set.of(testTag.getId()));
        NewsDtoResponse result = newsService.create(request);
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("New News");
        assertThat(result.author().authorId()).isEqualTo(testAuthor.getId());
        assertThat(result.tags()).hasSize(1);
        assertThat(result.tags()).extracting(TagDtoResponse::name).contains("Tech");
    }

    @Test
    void testUpdate() {
        NewsDtoRequest request = new NewsDtoRequest(testNews.getId(), "Updated News", "Updated Content",
                testAuthor.getId(), Set.of(testTag.getId()));
        NewsDtoResponse result = newsService.update(request);
        assertThat(result).isNotNull();
        assertThat(result.newsId()).isEqualTo(testNews.getId());
        assertThat(result.title()).isEqualTo("Updated News");
        assertThat(result.content()).isEqualTo("Updated Content");
    }

    @Test
    void testDeleteById() {
        boolean deleted = newsService.deleteById(testNews.getId());
        assertThat(deleted).isTrue();
        assertThat(newsRepositoryImpl.readById(testNews.getId())).isEmpty();
    }

    @Test
    void testFindNewsByParams() {
        List<NewsDtoResponse> result = newsService.findNewsByParams(
                List.of("Tech"), null, "John", "Test", "Content");
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).newsId()).isEqualTo(testNews.getId());
        assertThat(result.get(0).author().name()).isEqualTo("John Doe");
        assertThat(result.get(0).tags()).extracting(TagDtoResponse::name).contains("Tech");
    }

    @Test
    void testFindNewsByParamsNotFound() {
        assertThrows(NotFoundException.class, () ->
                newsService.findNewsByParams(List.of("Nonexistent"), null, null, null, null));
    }
}