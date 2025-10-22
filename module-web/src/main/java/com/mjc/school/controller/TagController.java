package com.mjc.school.controller;

import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;

import java.util.List;

public interface TagController extends BaseController<TagDtoRequest, TagDtoResponse,Long> {
    List<TagDtoResponse> findTagsByNewsId(@CommandParam(name ="id") Long id);
}
