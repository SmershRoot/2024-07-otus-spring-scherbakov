package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.TestData;
import ru.otus.hw.services.GenreService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с жанрами")
@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService service;

    @Test
    @DisplayName("Должен вернуть все жанры")
    public void testRead() throws Exception {
        var genres = TestData.getDbGenreDTOs();
        when(service.readAll()).thenReturn(genres);

        mockMvc.perform(get("/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }

}
