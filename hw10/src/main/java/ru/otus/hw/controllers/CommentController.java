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
            @PathVariable Long bookId
    ) {
        return service.readByBookId(bookId);
    }

    @GetMapping("/book/{bookId}/comment/{id}")
    public CommentDTO read(
            @PathVariable Long bookId,
            @PathVariable Long id
    ) {
        return service.readById(id);
    }

    @PostMapping("/book/{bookId}/comment")
    public CommentDTO create(
            @PathVariable Long bookId,
            CommentDTO commentDTO
    ) {
        return service.create(bookId, commentDTO);
    }

    @PutMapping("/book/{bookId}/comment/{id}")
    public CommentDTO update(
            @PathVariable Long bookId,
            @PathVariable Long id,
            CommentDTO commentDTO
    ) {
        return service.update(id, commentDTO);
    }

    @DeleteMapping("/book/{bookId}/comment/{id}")
    public void delete(
            @PathVariable Long bookId,
            @PathVariable Long id
    ) {
        service.deleteById(id);
    }
}
