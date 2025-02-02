package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repository;

    private final BookRepository bookRepository;

    private final CommentMapper mapper;


    @GetMapping("/book/{bookId}/comments")
    public Flux<CommentDTO> readAll(
            @PathVariable String bookId
    ) {
        var ff = repository.findAll();
        return repository.findAll().map(mapper::toCommentDTO);
    }

    @GetMapping("/book/{bookId}/comment")
    public Flux<CommentDTO> readByBookId(
            @PathVariable String bookId
    ) {
        bookRepository.findById(bookId);
        return repository.findAllByBookId(bookId).map(mapper::toCommentDTO);
    }

    @GetMapping("/book/{bookId}/comment/{id}")
    public CommentDTO readById(
            @PathVariable String id
    ) {
        return findById(id).map(mapper::toCommentDTO).block();
    }

    @PostMapping("/book/{bookId}/comment")
    public Mono<CommentDTO> create(
            @PathVariable String bookId,
            CommentDTO commentDTO
    ) {
        commentDTO.setId(null);
        var entity = mapper.toComment(commentDTO);
        return bookRepository.findById(bookId)
                .flatMap(book -> {
                    entity.setBook(book);
                    return repository.save(entity);
                }).map(mapper::toCommentDTO);
    }

    @PutMapping("/book/{bookId}/comment/{id}")
    public Mono<CommentDTO> update(
            @PathVariable String id,
            CommentDTO commentDTO
    ) {
        var entity = findById(id);
        return entity.flatMap(e ->  {
            mapper.updateCommentFromDto(e, commentDTO);
            return repository.save(e);
        }).map(mapper::toCommentDTO);
    }

    @DeleteMapping("/book/{bookId}/comment/{id}")
    public Mono<Void> delete(
            @PathVariable String id
    ) {
        return repository.deleteById(id);
    }

    public Mono<Comment> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(id))));
    }
}
