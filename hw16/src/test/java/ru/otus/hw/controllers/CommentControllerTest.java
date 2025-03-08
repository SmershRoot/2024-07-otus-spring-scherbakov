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
import ru.otus.hw.services.CommentService;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с комментариями книг")
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService service;

    @Test
    @DisplayName("Должен вернуть все комментарии книги")
    public void readByBookId() throws Exception {
        var comments = TestData.getDbCommentDTOs();
        when(service.readByBookId(1L)).thenReturn(comments);
        mockMvc.perform(get("/book/{bookId}/comment", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comments)));
    }

    @Test
    @DisplayName("Должен вернуть комментарий по id")
    public void readById() throws Exception {
        var comment = TestData.getDbCommentDTOs().get(0);
        when(service.readById(1L)).thenReturn(comment);
        mockMvc.perform(get("/book/{bookId}/comment/{id}", 1, comment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }


    @Test
    @DisplayName("Должен вернуть ошибку получения комментария по несуществующему ид")
    void readByIdNotFound() throws Exception {
        when(service.readById(100))
                .thenThrow(new EntityNotFoundException("Comment with id %d not found".formatted(100)));

        mockMvc.perform(get("/book/{bookId}/comment/{id}", 1, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен вернуть созданный комментарий")
    public void create() throws Exception {
        var comment = TestData.getDbCommentDTOs().get(0);
        when(service.create(1L, comment)).thenReturn(comment);
        mockMvc.perform(post("/book/{bookId}/comment", 1)
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Test
    @DisplayName("Должен вернуть обновленный комментарий")
    public void update() throws Exception {
        var comment = TestData.getDbCommentDTOs().get(0);
        when(service.update(comment.getId(), comment)).thenReturn(comment);
        mockMvc.perform(put("/book/{bookId}/comment/{id}", 1, comment.getId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Test
    @DisplayName("Должен удалить комментарий")
    public void deleteById() throws Exception {
        var comment = TestData.getDbCommentDTOs().get(0);
        mockMvc.perform(delete("/book/{bookId}/comment/{id}", 1, comment.getId()))
                .andExpect(status().isOk());
    }


}
