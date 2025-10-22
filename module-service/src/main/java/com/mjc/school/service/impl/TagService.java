package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.TagMapper;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.validator.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ServiceErrorCode.TAG_ID_DOES_NOT_EXIST;

@Service
public class TagService implements BaseService<TagDtoRequest, TagDtoResponse, Long> {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final BaseRepository<News,Long> newsRepository;


    @Autowired
    public TagService(TagRepository tagRepository, TagMapper tagMapper, BaseRepository<News,Long> newsRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDtoResponse> readAll() {
        return tagMapper.modelListToDtoList(tagRepository.readAll());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDtoResponse readById(Long id) {
        return tagRepository.readById(id).map(tagMapper::modelToDto)
                .orElseThrow(
                    () -> new NotFoundException(String.format(TAG_ID_DOES_NOT_EXIST.getMessage(), id)));

    }

    @Override
    @Transactional
    public TagDtoResponse create(@Valid TagDtoRequest createRequest) {
        Tag tag = tagRepository.create(tagMapper.dtoToModel(createRequest));
        return tagMapper.modelToDto(tag);
    }

    @Override
    @Transactional
    public TagDtoResponse update(@Valid TagDtoRequest updateRequest) {
        if(tagRepository.readById(updateRequest.tagId()).isPresent()) {
           Tag tag = tagRepository.update(tagMapper.dtoToModel(updateRequest));
           return tagMapper.modelToDto(tag);
        }else{
            throw new NotFoundException(String.format(TAG_ID_DOES_NOT_EXIST.getMessage(), updateRequest.tagId()));
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(tagRepository.readById(id).isPresent()) {
            return tagRepository.deleteById(id);
        }else{
            throw new NotFoundException(String.format(TAG_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    @Transactional(readOnly = true)
    public List<TagDtoResponse> findTagsByNewsId(Long id) {
        if(newsRepository.readById(id).isPresent()) {
            List<Tag> tags = tagRepository.findTagsByNewsId(id);
            return tagMapper.modelListToDtoList(tags);
        }else{
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

}
