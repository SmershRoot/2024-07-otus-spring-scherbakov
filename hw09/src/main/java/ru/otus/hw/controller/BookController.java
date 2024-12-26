package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    BookService bookService;

    AuthorService authorService;

    GenreService genreService;

    @GetMapping("/books")
    public String readAll(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String read(@PathVariable Long id, Model model) {
        if (id == 0) {
            model.addAttribute("book", new BookBasicDTO());
        } else {
            model.addAttribute("book", bookService.findBasicById(id));
        }
        model.addAttribute("all_authors", authorService.findAll());
        model.addAttribute("all_genres", genreService.findAll());

        return "book";
    }

    @PostMapping("/books/save")
    public String save(
            @RequestParam Long id,
            @Valid @ModelAttribute("book") BookBasicDTO book,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("all_authors", authorService.findAll());
            model.addAttribute("all_genres", genreService.findAll());
            return "book";
        }

        if (id == 0) {
            bookService.insert(book.getTitle(), book.getAuthorId(),
                    new HashSet<>(book.getGenreIds()));
        } else {
            bookService.update(book.getId(), book.getTitle(), book.getAuthorId(),
                    new HashSet<>(book.getGenreIds()));
        }
        return "redirect:/books";
    }

    @PostMapping("/books/delete")
    public String delete(
            @RequestParam Long id,
            @RequestParam(name = "_method", required = false) String method,
            @ModelAttribute BookDTO book) {
        if (method.equalsIgnoreCase("DELETE")) {
            delete(book.getId());
        }
        return "redirect:/books";
    }

    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }


}
