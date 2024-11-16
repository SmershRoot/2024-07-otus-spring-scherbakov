package ru.otus.hw.services;

import java.util.List;

import ru.otus.hw.dto.AuthorDTO;

public interface AuthorService {
    List<AuthorDTO> findAll();
}
