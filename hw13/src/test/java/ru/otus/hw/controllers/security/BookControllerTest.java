package ru.otus.hw.controllers.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.TestData;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.BookWithSecurityService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(BookServiceImpl.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookWithSecurityService securityService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void readTest() throws Exception {
        var bookDTOs = getDbBookDTOs();
        var books = getDbBooks();

        when(securityService.readAll()).thenReturn(books);
        when(bookMapper.toBookDTO(books.get(0))).thenReturn(bookDTOs.get(0));
        when(bookMapper.toBookDTO(books.get(1))).thenReturn(bookDTOs.get(1));
        when(bookMapper.toBookDTO(books.get(2))).thenReturn(bookDTOs.get(2));

        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTOs)));
    }

    @Test
    public void readByIdTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        var books = getDbBooks();

        when(securityService.findById(bookDTO.getId())).thenReturn(books.get(0));
        when(bookMapper.toBookDTO(books.get(0))).thenReturn(bookDTO);

        mockMvc.perform(get("/book/" + bookDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
    }

    @Test
    public void createTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        var books = getDbBooks();

        when(securityService.create((books.get(0)))).thenReturn(books.get(0));
        when(bookMapper.toBook(bookDTO)).thenReturn(books.get(0));
        when(bookMapper.toBookDTO(books.get(0))).thenReturn(bookDTO);

        mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
    }

    @Test
    public void updateTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        var books = getDbBooks();

        when(securityService.findById(bookDTO.getId())).thenReturn(books.get(0));
        when(bookMapper.toBook(bookDTO)).thenReturn(books.get(0));
        when(securityService.update(books.get(0))).thenReturn(books.get(0));
        when(bookMapper.toBookDTO(books.get(0))).thenReturn(bookDTO);

        mockMvc.perform(put("/book/" + bookDTO.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));

    }

    @Test
    public void deleteTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        mockMvc.perform(delete("/book/" + bookDTO.getId()))
                .andExpect(status().isOk());
    }

    private List<Book> getDbBooks() {
        return TestData.getDbBooks();
    }

    private List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }


}
