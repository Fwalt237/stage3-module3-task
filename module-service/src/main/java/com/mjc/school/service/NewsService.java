package com.mjc.school.service;

import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;

import java.util.List;

public interface NewsService extends BaseService<NewsDtoRequest, NewsDtoResponse,Long> {
    List<NewsDtoResponse> findNewsByParams(List<String> tagNames,
                                           List<Long> tagIds, String authorName,
                                           String title, String content);
}
