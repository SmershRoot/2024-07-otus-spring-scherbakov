package ru.otus.hw.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    BookService bookService;

    AuthorService authorService;

    GenreService genreService;

    @GetMapping("/book")
    public String readAll(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String read(@PathVariable Long id, Model model) {
        var book = bookService.findById(id);
        model.addAttribute("book", book.orElse(new BookDTO()));
        model.addAttribute("all_authors", authorService.findAll());
        model.addAttribute("all_genres", genreService.findAll());

        return "book";
    }

    @PostMapping("/book/{id}")
    public String save(
            @PathVariable(required = false) Long id,
            @RequestParam(name = "_method", required = false) String method,
            @ModelAttribute BookDTO book) {
        if (method.equalsIgnoreCase("POST")) {
            create(book);
        }
        if(method.equalsIgnoreCase("PUT")){
            update(book.getId(), book);
        }
        if(method.equalsIgnoreCase("DELETE")){
            delete(book.getId());
        }
        return "redirect:/book";
    }

    public void create(@ModelAttribute("createBook") BookDTO book) {
        var genreIds = book.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet());
        bookService.insert(book.getTitle(), book.getAuthor().getId(), genreIds);
    }

    public void update(@PathVariable Long id, @ModelAttribute("updateBook") BookDTO book) {
        var genreIds = book.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet());
        bookService.update(id, book.getTitle(), book.getAuthor().getId(), genreIds);
    }

    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }


}
