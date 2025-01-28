package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.TestData;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.configuration.constants.Constants;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    public void testWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Welcome to the library")));
    }

    @Test
    @WithMockUser
    public void readAll() throws Exception {
        var books = getDbBookDTOs();
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpectAll(
                        content().string(containsString(books.get(0).getTitle())),
                        content().string(containsString(books.get(1).getTitle())),
                        content().string(containsString(books.get(2).getTitle()))
                )
                .andExpect(model().attribute("books", books));
    }

    @Test
    public void readAllWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void readWithAdmin() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        when(bookService.findBasicById(1)).thenReturn(getBookBasicDTO(book));
        when(authorService.findAll()).thenReturn(getDbAuthorDTOs());
        when(genreService.findAll()).thenReturn(getDbGenreDTOs());

        mockMvc.perform(get("/books/1")
                .with(user("admin").authorities(new SimpleGrantedAuthority(Constants.Authority.ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString(book.getTitle())),
                        model().attribute("book", getBookBasicDTO(book)),
                        model().attribute("all_authors", getDbAuthorDTOs()),
                        model().attribute("all_genres", getDbGenreDTOs())
                )
                .andExpect(view().name("book"));
    }

    @Test
    @WithMockUser
    public void readWithUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        when(bookService.findBasicById(1)).thenReturn(getBookBasicDTO(book));
        when(authorService.findAll()).thenReturn(getDbAuthorDTOs());
        when(genreService.findAll()).thenReturn(getDbGenreDTOs());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString(book.getTitle())),
                        model().attribute("book", getBookBasicDTO(book)),
                        model().attribute("all_authors", getDbAuthorDTOs()),
                        model().attribute("all_genres", getDbGenreDTOs())
                )
                .andExpect(view().name("book"));
    }

    @Test
    public void readWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void readForCreate() throws Exception {
        when(authorService.findAll()).thenReturn(getDbAuthorDTOs());
        when(genreService.findAll()).thenReturn(getDbGenreDTOs());

        mockMvc.perform(get("/books/0")
                .with(user("admin").authorities(new SimpleGrantedAuthority(Constants.Authority.ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("book", new BookBasicDTO()),
                        model().attribute("all_authors", getDbAuthorDTOs()),
                        model().attribute("all_genres", getDbGenreDTOs())
                )
                .andExpect(view().name("book"));
    }

    @Test
    public void createWithAdmin() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                .param("id", "0")
                .flashAttr("book", newBook)
                .with(user("admin").authorities(new SimpleGrantedAuthority(Constants.Authority.ROLE_ADMIN))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService).insert(
                eq(book.getTitle()),
                eq(book.getAuthor().getId()),
                eq(book.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet()))
        );
    }

    @Test
    @WithMockUser
    public void createWithUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                        .param("id", "0")
                        .flashAttr("book", newBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    public void createWithoutAuthentication() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                        .param("id", "0")
                        .flashAttr("book", newBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void updateWithAdmin() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                        .param("id", String.valueOf(newBook.getId()))
                        .flashAttr("book", newBook)
                .with(user("admin").authorities(new SimpleGrantedAuthority(Constants.Authority.ROLE_ADMIN))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService).update(
                eq(book.getId()),
                eq(book.getTitle()),
                eq(book.getAuthor().getId()),
                eq(book.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet()))
        );
    }

    @Test
    @WithMockUser
    public void updateWithUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                        .param("id", String.valueOf(newBook.getId()))
                        .flashAttr("book", newBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    public void updateWithoutAuthentication() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        var newBook = getBookBasicDTO(book);

        mockMvc.perform(post("/books/save")
                        .param("id", String.valueOf(newBook.getId()))
                        .flashAttr("book", newBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void deleteWithAdmin() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(post("/books/delete")
                .param("id", String.valueOf(book.getId()))
                .param("_method", "delete")
                        .with(user("admin").authorities(new SimpleGrantedAuthority(Constants.Authority.ROLE_ADMIN))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteById(book.getId());
    }

    @Test
    @WithMockUser
    public void deleteWithUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(post("/books/delete")
                        .param("id", String.valueOf(book.getId()))
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    public void deleteWithoutAuthentication() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(post("/books/delete")
                        .param("id", String.valueOf(book.getId()))
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    private static BookBasicDTO getBookBasicDTO(BookDTO dto) {
        var result = new BookBasicDTO();
        result.setId(dto.getId());
        result.setTitle(dto.getTitle());
        result.setAuthorId(dto.getAuthor().getId());
        result.setGenreIds(dto.getGenres().stream().map(GenreDTO::getId).toList());
        return result;
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

    private static List<AuthorDTO> getDbAuthorDTOs() {
        return TestData.getDbAuthorDTOs();
    }

    private static List<GenreDTO> getDbGenreDTOs() {
        return TestData.getDbGenreDTOs();
    }
}