package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.impl.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @CommandHandler(operation = 7)
    public List<AuthorDtoResponse> readAll() {
        return authorService.readAll();
    }

    @Override
    @CommandHandler(operation = 8)
    public AuthorDtoResponse readById(@CommandParam(name ="id") Long id) {
        return authorService.readById(id);
    }

    @Override
    @CommandHandler(operation = 9)
    public AuthorDtoResponse create(@CommandBody AuthorDtoRequest createRequest) {
        return authorService.create(createRequest);
    }

    @Override
    @CommandHandler(operation = 10)
    public AuthorDtoResponse update(@CommandBody AuthorDtoRequest updateRequest) {
        return authorService.update(updateRequest);
    }

    @Override
    @CommandHandler(operation = 11)
    public boolean deleteById(@CommandParam(name ="id")  Long id) {
        return authorService.deleteById(id);
    }

    @CommandHandler(operation = 12)
    public AuthorDtoResponse findAuthorByNewsId(@CommandParam(name ="id") Long id){
        return authorService.findAuthorByNewsId(id);
    }
}
