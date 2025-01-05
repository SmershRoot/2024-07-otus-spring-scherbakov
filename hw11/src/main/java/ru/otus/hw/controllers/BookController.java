package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    private final BookMapper mapper;

    private final BookRepository repository;

    @GetMapping("/book")
    public Flux<BookDTO> read() {
        return repository.findAll().map(mapper::toBookDTO);
    }

    @GetMapping("/book/{id}")
    public Mono<BookDTO> readById(
            @PathVariable String id
    ) {
        return findById(id).map(mapper::toBookDTO);
    }

    @PostMapping("/book")
    public Mono<BookDTO> create(
            @RequestBody @Valid BookDTO book
    ) {
        var entity = mapper.toBook(book);
        var mono = repository.save(entity);
        return mono.map(mapper::toBookDTO);
    }

    @PutMapping("/book/{id}")
    public Mono<BookDTO> update(
            @PathVariable String id,
            @RequestBody @Valid BookDTO book
    ) {
        var entity = findById(id);
        return entity.flatMap(e ->  {
            mapper.updateBookFromDto(e, book);
            return repository.save(e);
        }).map(mapper::toBookDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }


    public Mono<Book> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }
}
