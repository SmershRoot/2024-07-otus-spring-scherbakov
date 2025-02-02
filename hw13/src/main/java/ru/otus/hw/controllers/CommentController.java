package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

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
