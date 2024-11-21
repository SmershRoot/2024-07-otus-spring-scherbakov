package ru.otus.hw.services;

import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Book findById(long id);

    BookDTO readById(long id);

    List<BookDTO> readAll();

    BookDTO create(BookDTO bookDTO);

    BookDTO update(long id, BookDTO bookDTO);

    void deleteById(long id);
}
