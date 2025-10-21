package com.mjc.school.service;

import com.mjc.school.repository.model.Author;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface AuthorMapper {
    List<AuthorDtoResponse> modelListToDtoList(List<Author> modelList);

    AuthorDtoResponse modelToDto(Author model);

    @Mapping(target="newsFromAuthor", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target="lastUpdateTime", ignore = true)
    Author dtoToModel(AuthorDtoRequest dto);
}
