package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.models.h2.CommentJpa;
import ru.otus.hw.models.mongo.CommentMongo;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    CommentJpa fromMongo(CommentMongo comment);

}
