package ru.otus.hw.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService service;

    @GetMapping("/book/{bookId}/comment")
    public List<CommentDTO> read(
            @PathVariable String bookId
    ) {
        return service.readByBookId(bookId);
    }

    @GetMapping("/book/{bookId}/comment/{id}")
    public CommentDTO read(
            @PathVariable String bookId,
            @PathVariable String id
    ) {
        return service.readById(id);
    }

    @PostMapping("/book/{bookId}/comment")
    public CommentDTO create(
            @PathVariable String bookId,
            CommentDTO commentDTO
    ) {
        return service.create(bookId, commentDTO);
    }

    @PutMapping("/book/{bookId}/comment/{id}")
    public CommentDTO update(
            @PathVariable String bookId,
            @PathVariable String id,
            CommentDTO commentDTO
    ) {
        return service.update(id, commentDTO);
    }

    @DeleteMapping("/book/{bookId}/comment/{id}")
    public void delete(
            @PathVariable String bookId,
            @PathVariable String id
    ) {
        service.deleteById(id);
    }
}
