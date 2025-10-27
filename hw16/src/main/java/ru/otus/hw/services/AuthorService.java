package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> findAll();

    List<Author> findAllWithoutBooks();
}
