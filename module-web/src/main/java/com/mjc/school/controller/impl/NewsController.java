package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class NewsController implements BaseController<NewsDtoRequest, NewsDtoResponse, Long> {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    @CommandHandler(operation = 1)
    public List<NewsDtoResponse> findAll() {
        return newsService.findAll();
    }

    @Override
    @CommandHandler(operation = 2)
    public NewsDtoResponse findById(@CommandParam(name="id") Long id) {
        return newsService.findById(id);
    }

    @Override
    @CommandHandler(operation = 3)
    public NewsDtoResponse create(@CommandBody NewsDtoRequest createRequest) {
        return newsService.create(createRequest);
    }

    @Override
    @CommandHandler(operation = 4)
    public NewsDtoResponse update(@CommandBody NewsDtoRequest updateRequest) {
        return newsService.update(updateRequest);
    }

    @Override
    @CommandHandler(operation = 5)
    public boolean deleteById(Long id) {
        return newsService.deleteById(id);
    }

    @CommandHandler(operation = 6)
    public List<NewsDtoResponse> findNewsByParams(@CommandParam(name="tagNames") List<String> tagNames,
                                                  @CommandParam(name="tagIds") List<Long> tagIds,
                                                  @CommandParam(name ="authorName") String authorName,
                                                  @CommandParam(name="title") String title,
                                                  @CommandParam(name="content")String content) {
        return newsService.findNewsByParams(tagNames, tagIds, authorName, title, content);
    }
}
