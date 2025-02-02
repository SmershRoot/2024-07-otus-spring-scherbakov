package ru.otus.hw.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class BookHandler {

    private final BookMapper mapper;

    private final BookRepository repository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    public BookHandler(
            BookMapper mapper,
            BookRepository repository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            CommentRepository commentRepository
    ) {
        this.mapper = mapper;
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    public Mono<ServerResponse> read(ServerRequest request) {
        Flux<BookDTO> books = repository.findAll().map(mapper::toBookDTO);
        return ok().contentType(APPLICATION_JSON).body(books, BookDTO.class);
    }

    public Mono<ServerResponse> readById(ServerRequest request) {
        Mono<BookDTO> book = findById(request.pathVariable("id")).map(mapper::toBookDTO);

        return ok().contentType(APPLICATION_JSON).body(book, BookDTO.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<BookDTO> book = request.bodyToMono(BookDTO.class);
        Mono<BookDTO> bookToSave = checkBook(book)
                .map(mapper::toBook)
                .flatMap(repository::save).map(mapper::toBookDTO);

        return ok().contentType(APPLICATION_JSON).body(bookToSave, BookDTO.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Book> entity = findById(request.pathVariable("id"));
        Mono<BookDTO> view = request.bodyToMono(BookDTO.class);
        view = checkBook(view);

        Mono<BookDTO> bookToSave = Mono.zip(entity, view).flatMap(tuple -> {
            Book book = tuple.getT1();
            BookDTO dto = tuple.getT2();
            mapper.updateBookFromDto(book, dto);
            return repository.save(book);
        }).map(mapper::toBookDTO);

        return ok().contentType(APPLICATION_JSON).body(bookToSave, BookDTO.class);
    }

    public Mono<BookDTO> checkBook(Mono<BookDTO> book) {
        return book.flatMap(dto -> Flux.fromIterable(dto.getGenres())
                        .flatMap(g -> genreRepository.findById(g.getId())
                                .switchIfEmpty(
                                        Mono.error(
                                                new EntityNotFoundException("Genre with id not found")))
                        )
                        .filter(Objects::nonNull)
                        .next().then(Mono.just(dto))
                )
                .flatMap(dto -> {
                    var author = dto.getAuthor();
                    if(Objects.isNull(author)){
                        return Mono.error(new RuntimeException("Author is null"));
                    }

                    return authorRepository.findById(author.getId())
                            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id not found")))
                            .then(Mono.just(dto));
                });
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        commentRepository.deleteAllByBookId(id).subscribe();
        Mono<Void> bookToDelete = repository.deleteById(id);
        return bookToDelete.then(ok().build());
    }

    private Mono<Book> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }

}
