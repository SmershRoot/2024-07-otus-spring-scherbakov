package ru.otus.hw.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class BookHandler {

    private final BookMapper mapper;

    private final BookRepository repository;

    private final CommentRepository commentRepository;

    public BookHandler(
            BookMapper mapper,
            BookRepository repository,
            CommentRepository commentRepository
    ) {
        this.mapper = mapper;
        this.repository = repository;
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
        Mono<BookDTO> bookToSave = book.map(mapper::toBook)
                .flatMap(repository::save).map(mapper::toBookDTO);
        return ok().contentType(APPLICATION_JSON).body(bookToSave, BookDTO.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Book> entity = findById(request.pathVariable("id"));
        Mono<BookDTO> view = request.bodyToMono(BookDTO.class);
        Mono<BookDTO> bookToSave = Mono.zip(entity, view).flatMap(tuple -> {
            Book book = tuple.getT1();
            BookDTO dto = tuple.getT2();
            mapper.updateBookFromDto(book, dto);
            return repository.save(book);
        }).map(mapper::toBookDTO);

        return ok().contentType(APPLICATION_JSON).body(bookToSave, BookDTO.class);
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
