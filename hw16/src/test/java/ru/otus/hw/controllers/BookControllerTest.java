package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.TestData;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с книгами")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("Должен вернуть все книги")
    public void read() throws Exception {
        var books = TestData.getDbBookDTOs();
        when(service.readAll()).thenReturn(books);

        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("Должен вернуть книгу по ид")
    void readById() throws Exception {
        var book = TestData.getDbBookDTOs().get(0);
        when(service.readById(book.getId())).thenReturn(book);
        mockMvc.perform(get("/book/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Должен вернуть ошибку получения книги по несуществующему ид")
    void readByIdNotFound() throws Exception {
        when(service.readById(100)).thenThrow(new EntityNotFoundException("Book with id 100 not found"));

        mockMvc.perform(get("/book/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен вернуть созданную книгу")
    void create() throws Exception {
        var book = TestData.getDbBookDTOs().get(0);
        when(service.create(book)).thenReturn(book);
        mockMvc.perform(post("/book")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Должен вернуть обновляемую книгу")
    void update() throws Exception {
        var book = TestData.getDbBookDTOs().get(0);
        when(service.update(book.getId(), book)).thenReturn(book);
        mockMvc.perform(put("/book/" + book.getId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Должен удалить книгу")
    void deleteById() throws Exception {
        var book = TestData.getDbBookDTOs().get(0);
        mockMvc.perform(delete("/book/{id}", book.getId()))
                .andExpect(status().isOk());
    }

}
