package ru.otus.hw.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreController {

    private final GenreRepository genreRepository;

    private final GenreMapper mapper;

    @GetMapping("/genre")
    public Flux<GenreDTO> read() {
        return genreRepository.findAll().map(mapper::toGenreDTO);
    }

}
