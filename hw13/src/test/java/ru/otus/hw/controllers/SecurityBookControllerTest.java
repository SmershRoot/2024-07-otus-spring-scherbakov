package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void readAllWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }


    @Test
    @WithMockUser(roles = "USER")
    void readAllUser() throws Exception {
        var books = getDbBookDTOs();
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void readAllEditor() throws Exception {
        var books = getDbBookDTOs().subList(0,1);
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void readByIdUser() throws Exception {
        var book = getDbBookDTOs().get(0);
        mockMvc.perform(get("/book/"+book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void readByIdEditorWithRight() throws Exception {
        var book = getDbBookDTOs().get(0);
        mockMvc.perform(get("/book/"+book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    @WithMockUser(username = "editor2", roles = "EDITOR")
    void readByIdEditorWithoutRight() throws Exception {
        var book = getDbBookDTOs().get(0);
        mockMvc.perform(get("/book/"+book.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        book.setId(null);

        mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void createEditor() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        book.setId(null);

        mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void createEditorAndAccessCheck() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);
        book.setId(null);

        String result = mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var bookDTO = mapper.readValue(result, BookDTO.class);
        mockMvc.perform(get("/book/" + bookDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
        mockMvc.perform(get("/book/" + bookDTO.getId())
                        .with(user("user1"))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
        mockMvc.perform(get("/book/" + bookDTO.getId())
                        .with(user("editor2")
                                .authorities(new SimpleGrantedAuthority("ROLE_EDITOR"))
                        )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void updateEditor() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(delete("/book/" + book.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void deleteEditor() throws Exception {
        var books = getDbBookDTOs();
        var book = books.get(0);

        mockMvc.perform(delete("/book/" + book.getId()))
                .andExpect(status().isOk());
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

}
