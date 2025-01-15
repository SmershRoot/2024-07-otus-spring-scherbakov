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
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping("/book")
    public List<BookDTO> read() {
        return service.readAll();
    }

    @GetMapping("/book/{id}")
    public BookDTO readById(
            @PathVariable Long id
    ) {
        return service.readById(id);
    }

    @PostMapping("/book")
    public BookDTO create(
            @RequestBody @Valid BookDTO book
    ) {
        return service.create(book);
    }

    @PutMapping("/book/{id}")
    public BookDTO update(
            @PathVariable Long id,
            @RequestBody @Valid BookDTO book
    ) {
        return service.update(id, book);
    }

    @DeleteMapping("/book/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}
