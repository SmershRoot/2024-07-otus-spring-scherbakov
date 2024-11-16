package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toBookDTO(Book book);

}
