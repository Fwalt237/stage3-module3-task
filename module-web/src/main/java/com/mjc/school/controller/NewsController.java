package com.mjc.school.controller;

import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;

import java.util.List;

public interface NewsController extends BaseController<NewsDtoRequest, NewsDtoResponse,Long> {
    List<NewsDtoResponse> findNewsByParams(@CommandParam(name="tagNames") List<String> tagNames,
                                           @CommandParam(name="tagIds") List<Long> tagIds,
                                           @CommandParam(name ="authorName") String authorName,
                                           @CommandParam(name="title") String title,
                                           @CommandParam(name="content")String content);
}
