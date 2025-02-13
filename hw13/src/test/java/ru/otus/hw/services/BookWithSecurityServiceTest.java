package ru.otus.hw.services;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.repositories.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static ru.otus.hw.TestData.getDbBooks;

@SpringBootTest
public class BookWithSecurityServiceTest {

    @Autowired
    private BookWithSecurityService service;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private MutableAclService mutableAclService;

    @Test
    @WithMockUser(roles = "USER")
    void readAll() {
        var books = getDbBooks().subList(1,3);
        when(bookRepository.findAll()).thenReturn(books);
        assertDoesNotThrow(() -> service.readAll());
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void readAllEditor() {
        var books = getDbBooks();
        when(bookRepository.findAll()).thenReturn(books);
        assertDoesNotThrow(() -> service.readAll());
        var editBooks = service.readAll();
        assertThat(editBooks).containsExactlyElementsOf(books.subList(0,1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByIdUser() {
        var book = getDbBooks().get(0);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> service.findById(book.getId()));
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void findByIdEditorWithRight() {
        var book = getDbBooks().get(0);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> service.findById(book.getId()));
    }

    @Test
    @WithMockUser(username = "editor2", roles = "EDITOR")
    void findByIdEditorWithoutRight() {
        var book = getDbBooks().get(0);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertThatThrownBy(() -> service.findById(book.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createUser() {
        var book = getDbBooks().get(0);
        book.setId(0);
        assertThatThrownBy(() -> service.create(book))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void createEditor() {
        var book = getDbBooks().get(0);
        when(bookRepository.save(book)).thenReturn(book);
        assertDoesNotThrow(() -> service.create(book));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser() {
        var book = getDbBooks().get(0);
        assertThatThrownBy(() -> service.update(book))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void updateEditor() {
        var book = getDbBooks().get(0);
        assertDoesNotThrow(() -> service.update(book));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteByIdUser() {
        var book = getDbBooks().get(0);
        assertThatThrownBy(() -> service.deleteById(book.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void deleteByIdEditor() {
        var book = getDbBooks().get(0);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> service.deleteById(book.getId()));
    }

}
