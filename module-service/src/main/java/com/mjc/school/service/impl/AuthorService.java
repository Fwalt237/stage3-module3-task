package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.service.AuthorMapper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.AUTHOR_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_ID_DOES_NOT_EXIST;

@Service
public class AuthorService implements BaseService<AuthorDtoRequest, AuthorDtoResponse,Long> {

    private final AuthorRepository authorRepository;
    private final NewsRepository newsRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository,NewsRepository newsRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDtoResponse> findAll() {
        return authorMapper.modelListToDtoList(authorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDtoResponse findById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::modelToDto)
                .orElseThrow(
                        () -> new NotFoundException(String
                                .format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), id)));
    }

    @Override
    @Transactional
    public AuthorDtoResponse create(@Valid AuthorDtoRequest createRequest) {
        Author author = authorRepository.create(authorMapper.dtoToModel(createRequest));
        return authorMapper.modelToDto(author);
    }

    @Override
    @Transactional
    public AuthorDtoResponse update(@Valid AuthorDtoRequest updateRequest) {
        if(authorRepository.findById(updateRequest.authorId()).isPresent()){
            Author author = authorRepository.update(authorMapper.dtoToModel(updateRequest));
            return authorMapper.modelToDto(author);
        }else {
            throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), updateRequest.authorId()));
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(authorRepository.findById(id).isPresent()){
            return authorRepository.deleteById(id);
        }else {
            throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    @Transactional(readOnly = true)
    public AuthorDtoResponse findAuthorByNewsId(Long id){
        if(newsRepository.findById(id).isPresent()){
            Author author = authorRepository.findAuthorByNewsId(id);
            return authorMapper.modelToDto(author);
        } else {
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

}
