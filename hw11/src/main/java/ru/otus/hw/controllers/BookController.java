package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookMapper mapper;

    private final BookRepository repository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

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
        var entity = Mono.just(mapper.toBook(book));
        return checkBook(entity)
                .flatMap(repository::save).map(mapper::toBookDTO);
    }

    @PutMapping("/book/{id}")
    public Mono<BookDTO> update(
            @PathVariable String id,
            @RequestBody @Valid BookDTO book
    ) {
        var entity = findById(id).map(e ->  {
            mapper.updateBookFromDto(e, book);
            return e;
        });
        return checkBook(entity)
                .flatMap(repository::save).map(mapper::toBookDTO);
    }

    @DeleteMapping("/book/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return repository.deleteById(id).then(commentRepository.deleteAllByBookId(id));
    }

    public Mono<Book> checkBook(Mono<Book> book) {
        return book.flatMap(entity -> Flux.fromIterable(entity.getGenres())
                        .flatMap(g -> genreRepository.findById(g.getId())
                                .switchIfEmpty(
                                        Mono.error(
                                                new EntityNotFoundException("Genre with id not found")))
                        )
                        .filter(Objects::nonNull)
                        .next().then(Mono.just(entity))
                )
                .flatMap(entity -> {
                    var author = entity.getAuthor();
                    if (Objects.isNull(author)) {
                        return Mono.error(new RuntimeException("Author is null"));
                    }

                    return authorRepository.findById(author.getId())
                            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id not found")))
                            .then(Mono.just(entity));
                });
    }

    private Mono<Book> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }
}
