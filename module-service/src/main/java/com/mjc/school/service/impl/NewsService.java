package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.NewsMapper;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_ID_DOES_NOT_EXIST;

@Service
public class NewsService implements BaseService<NewsDtoRequest, NewsDtoResponse, Long> {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository, NewsMapper newsMapper, AuthorRepository authorRepository, TagRepository tagRepository) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDtoResponse> findAll() {
        return newsMapper.modelListToDtoList(newsRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDtoResponse findById(Long id) {
        return newsRepository.findById(id).map(newsMapper::modelToDto)
                .orElseThrow(
                    () -> new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id))
                );
    }

    @Override
    @Transactional
    public NewsDtoResponse create(@Valid NewsDtoRequest createRequest) {
        News news = newsRepository.create(newsMapper.dtoToModel(createRequest, authorRepository, tagRepository));
        return newsMapper.modelToDto(news);
    }

    @Override
    @Transactional
    public NewsDtoResponse update(@Valid NewsDtoRequest updateRequest) {
        if(newsRepository.findById(updateRequest.newsId()).isPresent()) {
            News news = newsRepository.update(newsMapper.dtoToModel(updateRequest, authorRepository, tagRepository));
            return newsMapper.modelToDto(news);
        }else{
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), updateRequest.newsId()));
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(newsRepository.findById(id).isPresent()) {
            return newsRepository.deleteById(id);
        }else{
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    @Transactional(readOnly = true)
    public List<NewsDtoResponse> findNewsByParams(List<String> tagNames,
                                                  List<Long> tagIds, String authorName,
                                                  String title, String content){

        List<News> news = newsRepository.findNewsByParams(tagNames, tagIds, authorName, title, content);
        if(!news.isEmpty()){
            return newsMapper.modelListToDtoList(news);
        }else{
            throw new NotFoundException("No News found.");
        }
    }

}
