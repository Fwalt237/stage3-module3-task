package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
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
    private final BaseRepository<News,Long> newsRepository;
    private final NewsMapper newsMapper;
    private final BaseRepository<Author,Long> authorRepository;
    private final BaseRepository<Tag,Long> tagRepository;

    private final NewsRepository nr = new NewsRepository();

    @Autowired
    public NewsService(BaseRepository<News,Long> newsRepository, NewsMapper newsMapper, BaseRepository<Author,Long>authorRepository, BaseRepository<Tag,Long> tagRepository) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDtoResponse> readAll() {
        return newsMapper.modelListToDtoList(newsRepository.readAll());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDtoResponse readById(Long id) {
        return newsRepository.readById(id).map(newsMapper::modelToDto)
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
        if(newsRepository.readById(updateRequest.newsId()).isPresent()) {
            News news = newsRepository.update(newsMapper.dtoToModel(updateRequest, authorRepository, tagRepository));
            return newsMapper.modelToDto(news);
        }else{
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), updateRequest.newsId()));
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(newsRepository.readById(id).isPresent()) {
            return newsRepository.deleteById(id);
        }else{
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    @Transactional(readOnly = true)
    public List<NewsDtoResponse> findNewsByParams(List<String> tagNames,
                                                  List<Long> tagIds, String authorName,
                                                  String title, String content){

        List<News> news = nr.findNewsByParams(tagNames, tagIds, authorName, title, content);
        if(!news.isEmpty()){
            return newsMapper.modelListToDtoList(news);
        }else{
            throw new NotFoundException("No News found.");
        }
    }

}
