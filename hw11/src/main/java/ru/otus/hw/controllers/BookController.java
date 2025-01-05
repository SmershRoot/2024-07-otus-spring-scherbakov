package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    BookService service;

    @GetMapping("/book")
    public List<BookDTO> read() {
        return service.readAll();
    }

    @GetMapping("/book/{id}")
    public BookDTO readById(
            @PathVariable String id
    ) {
        return service.readById(id);
    }

    @PostMapping("/book")
    public BookDTO create(
            @RequestBody @Valid BookDTO book
    ) {
        return service.create(book);
    }

    @PostMapping("/book/{id}")
    public BookDTO update(
            @PathVariable String id,
            @RequestBody @Valid BookDTO book
    ) {
        return service.update(id, book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }

}
