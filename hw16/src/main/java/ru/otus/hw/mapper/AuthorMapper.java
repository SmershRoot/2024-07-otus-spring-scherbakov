package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDTO toAuthorDTO(Author author);

    Author toAuthor(AuthorDTO authorDTO);

}
