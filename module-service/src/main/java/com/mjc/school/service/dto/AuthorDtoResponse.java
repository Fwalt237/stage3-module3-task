package com.mjc.school.service.dto;

import java.time.LocalDateTime;

public record AuthorDtoResponse(
    Long authorId,
    String name,
    LocalDateTime createDate,
    LocalDateTime lastUpdateTime) {}
