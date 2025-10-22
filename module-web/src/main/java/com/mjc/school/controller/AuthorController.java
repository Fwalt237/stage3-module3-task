package com.mjc.school.controller;

import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;

public interface AuthorController extends BaseController<AuthorDtoRequest, AuthorDtoResponse,Long> {
    AuthorDtoResponse findAuthorByNewsId(@CommandParam(name ="id") Long id);
}
