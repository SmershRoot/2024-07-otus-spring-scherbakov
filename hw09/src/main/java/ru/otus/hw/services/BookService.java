package ru.otus.hw.services;

import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDTO findById(long id);

    BookBasicDTO findBasicById(long id);

    List<BookDTO> findAll();

    BookDTO insert(String title, long authorId, Set<Long> genresIds);

    BookDTO update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);
}
