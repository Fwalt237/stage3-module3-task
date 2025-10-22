package com.mjc.school.repository;

import com.mjc.school.repository.model.News;

import java.util.List;

public interface NewsRepository extends BaseRepository<News,Long>{
    List<News> findNewsByParams(List<String> tagNames, List<Long> tagIds,
                                String authorName, String title, String content);
}
