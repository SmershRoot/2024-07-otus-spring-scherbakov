package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.mongo.AuthorMongo;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    AuthorJpa toAuthorJpa(AuthorMongo author);

}
