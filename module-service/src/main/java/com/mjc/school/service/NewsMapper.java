package com.mjc.school.service;


import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel="spring", uses = {AuthorMapper.class, TagMapper.class})
public interface NewsMapper {

    List<NewsDtoResponse> modelListToDtoList(List<News> modelList);


    NewsDtoResponse modelToDto(News model);

    @Mapping(target = "author", source="authorId", qualifiedByName = "mapAuthorIdToAuthor")
    @Mapping(target = "tagsFromNews", source = "tagIds", qualifiedByName = "mapTagIdsToTags")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateTime", ignore = true)
    News dtoToModel(NewsDtoRequest dto, @Context BaseRepository<Author,Long> authorRepository, @Context BaseRepository<Tag,Long> tagRepository);

    @Named("mapAuthorIdToAuthor")
    default Author mapAuthorIdToAuthor(Long authorId, @Context BaseRepository<Author,Long> authorRepository) {
        return authorId != null ? authorRepository.readById(authorId).orElse(null) : null;
    }

    @Named("mapTagIdsToTags")
    default Set<Tag> mapTagIdsToTags(Set<Long> tagIds, @Context BaseRepository<Tag,Long> tagRepository) {
        if(tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }
        return tagIds.stream().map(id->tagRepository.readById(id).orElse(null))
        .filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
