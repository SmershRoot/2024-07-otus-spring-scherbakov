package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDTO toGenreDTO(Genre genre);

}
