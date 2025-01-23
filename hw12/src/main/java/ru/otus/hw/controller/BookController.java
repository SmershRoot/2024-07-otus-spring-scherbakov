package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.configuration.constants.Constants;
import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;

@Controller
@RequiredArgsConstructor
public class BookController {

   private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String readAll(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("isAdmin", isAdmin());
        return "books";
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(Constants.Authority.ROLE_ADMIN));
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
    @PreAuthorize(Constants.Authority.PREAUTHORIZE_ADMIN)
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
    @PreAuthorize(Constants.Authority.PREAUTHORIZE_ADMIN)
    public String delete(
            @RequestParam Long id,
            @RequestParam(name = "_method", required = false) String method,
            @ModelAttribute BookDTO book) {
        if (method.equalsIgnoreCase("DELETE")) {
            delete(book.getId());
        }
        return "redirect:/books";
    }

    private void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleException(AccessDeniedException e) {
        return "forbidden";
    }
}
