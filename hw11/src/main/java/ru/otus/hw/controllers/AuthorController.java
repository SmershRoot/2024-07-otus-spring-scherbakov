package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorMapper mapper;

    private final AuthorRepository authorRepository;

    @GetMapping("/author")
    public Flux<AuthorDTO> read() {
        return authorRepository.findAll().map(mapper::toAuthorDTO);
    }

}
