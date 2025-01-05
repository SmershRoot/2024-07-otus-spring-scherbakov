package ru.otus.hw.controllers;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController {

    private final AuthorMapper mapper;

    AuthorRepository authorRepository;

    @GetMapping("/author")
    public Flux<AuthorDTO> read() {
        return authorRepository.findAll().map(mapper::toAuthorDTO);
    }

}
