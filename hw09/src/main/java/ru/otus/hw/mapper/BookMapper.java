package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;


@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toBookDTO(Book book);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(expression = "java(book.getGenres().stream().map(Genre::getId).toList())",
            target = "genreIds")
    BookBasicDTO toBookBasicDTO(Book book);

}
