package com.mjc.school.service;

import com.mjc.school.repository.impl.NewsRepositoryImpl;
import com.mjc.school.repository.impl.TagRepositoryImpl;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TagServiceImplTest {

     private TagServiceImpl tagServiceImpl;
     private TagRepositoryImpl tagRepositoryImpl;
     private NewsRepositoryImpl newsRepositoryImpl;

    private Tag testTag;
    private News testNews;

    @BeforeEach
    @Transactional
    void setUp() {
        testTag = new Tag();
        testTag.setName("Sports");
        testTag = tagRepositoryImpl.create(testTag);

        testNews = new News();
        testNews.setTitle("News");
        testNews.setContent("Content");
        testNews.setTagsFromNews(Set.of(testTag));
        testNews = newsRepositoryImpl.create(testNews);
    }

    @Test
    void testReadAll() {
        List<TagDtoResponse> result = tagServiceImpl.readAll();
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(dto -> dto.name().equals("Sports"));
    }

    @Test
    void testReadById() {
        TagDtoResponse result = tagServiceImpl.readById(testTag.getId());
        assertThat(result).isNotNull();
        assertThat(result.tagId()).isEqualTo(testTag.getId());
        assertThat(result.name()).isEqualTo("Sports");
    }

    @Test
    void testCreate() {
        TagDtoRequest request = new TagDtoRequest(null, "Tech");
        TagDtoResponse result = tagServiceImpl.create(request);
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Tech");
        assertThat(tagRepositoryImpl.readById(result.tagId())).isPresent();
    }

    @Test
    void testUpdate() {
        TagDtoRequest request = new TagDtoRequest(testTag.getId(), "Updated Sports");
        TagDtoResponse result = tagServiceImpl.update(request);
        assertThat(result).isNotNull();
        assertThat(result.tagId()).isEqualTo(testTag.getId());
        assertThat(result.name()).isEqualTo("Updated Sports");
    }

    @Test
    void testDeleteById() {
        boolean deleted = tagServiceImpl.deleteById(testTag.getId());
        assertThat(deleted).isTrue();
        assertThat(tagRepositoryImpl.readById(testTag.getId())).isEmpty();
    }

    @Test
    void testFindTagsByNewsId() {
        List<TagDtoResponse> result = tagServiceImpl.findTagsByNewsId(testNews.getId());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Sports");
    }

    @Test
    void testFindTagsByNewsIdNotFound() {
        assertThrows(NotFoundException.class, () -> tagServiceImpl.findTagsByNewsId(999L));
    }
}