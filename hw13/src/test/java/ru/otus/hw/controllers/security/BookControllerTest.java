package ru.otus.hw.controllers.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.BookWithSecurityServiceMockUtilTest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(BookServiceImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest extends BookWithSecurityServiceMockUtilTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        super.addMock();
    }

    @Test
    public void readTest() throws Exception {
        var bookDTOs = getDbBookDTOs();
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTOs)));
    }

    @Test
    public void readByIdTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        mockMvc.perform(get("/book/" + bookDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
    }

    @Test
    public void createTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
        mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
    }

    @Test
    public void updateTest() throws Exception {
        var bookDTO = getDbBookDTOs().get(0);
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

}
