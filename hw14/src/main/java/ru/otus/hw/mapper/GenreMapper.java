package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.models.h2.GenreJpa;
import ru.otus.hw.models.mongo.GenreMongo;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    GenreJpa toJpa(GenreMongo genre);

}
