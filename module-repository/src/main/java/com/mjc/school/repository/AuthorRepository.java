package com.mjc.school.repository;

import com.mjc.school.repository.model.Author;

public interface AuthorRepository extends BaseRepository<Author,Long>{
    Author findAuthorByNewsId(Long newsId);
}
