package ru.otus.hw.services;

import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    BookDTO readById(long id);

    List<BookDTO> readAll();

//    List<BookDTO> mapAll(List<Book> books);

    BookDTO create(BookDTO bookDTO);

    BookDTO update(long id, BookDTO bookDTO);

    void deleteById(long id);
}
