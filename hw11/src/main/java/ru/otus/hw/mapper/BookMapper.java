package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toBookDTO(Book book);

    Book toBook(BookDTO dto);

    void updateBookFromDto(@MappingTarget Book entity, BookDTO dto);

    Author map(AuthorDTO dto);

}
