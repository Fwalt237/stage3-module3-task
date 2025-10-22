package com.mjc.school.controller.impl;

import com.mjc.school.controller.TagController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Autowired
    public TagControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    @CommandHandler(operation = 13)
    public List<TagDtoResponse> readAll() {
        return tagService.readAll();
    }

    @Override
    @CommandHandler(operation = 14)
    public TagDtoResponse readById(@CommandParam(name="id") Long id) {
        return tagService.readById(id);
    }

    @Override
    @CommandHandler(operation = 15)
    public TagDtoResponse create(@CommandBody TagDtoRequest createRequest) {
        return tagService.create(createRequest);
    }

    @Override
    @CommandHandler(operation = 16)
    public TagDtoResponse update(@CommandBody TagDtoRequest updateRequest) {
        return tagService.update(updateRequest);
    }

    @Override
    @CommandHandler(operation = 17)
    public boolean deleteById(@CommandParam(name="id") Long id) {
        return tagService.deleteById(id);
    }

    @CommandHandler(operation = 18)
    public List<TagDtoResponse> findTagsByNewsId(@CommandParam(name ="id") Long id){
        return tagService.findTagsByNewsId(id);
    }
}
