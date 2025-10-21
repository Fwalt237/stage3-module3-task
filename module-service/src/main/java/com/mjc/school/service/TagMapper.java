package com.mjc.school.service;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface TagMapper {

    List<TagDtoResponse> modelListToDtoList(List<Tag> modelList);

    TagDtoResponse modelToDto(Tag model);

    @Mapping(target="newsFromTag", ignore = true)
    Tag dtoToModel(TagDtoRequest dto);
}
