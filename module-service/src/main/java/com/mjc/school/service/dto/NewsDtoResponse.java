package com.mjc.school.service.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record NewsDtoResponse(
        Long newsId,
        String title,
        String content,
        LocalDateTime createDate,
        LocalDateTime lastUpdateTime,
        AuthorDtoResponse author,
        Set<TagDtoResponse> tags) {}
