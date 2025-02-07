package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityBookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper mapper;

    @ParameterizedTest(name = "{0}. {1} {2} for user {3} with roles {4} should return {5} status and redirect = {6} or {7}")
    @MethodSource("getTestData")
    public void shouldReturnExpectedStatus(
            int number,
            String method,
            String uri,
            String username,
            String[] roles,
            ResultMatcher status,
            String redirectedUrl,
            String redirectedUrlPattern,
            BookDTO content,
            Object returnedObject) throws Exception {
        var books = getDbBooks();
        when(bookRepository.findAll()).thenReturn(books);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(bookRepository.save(books.get(0))).thenReturn(books.get(0));

        var request = method2RequestBuilder(method, uri);

        if (Objects.nonNull(username)) {
            var user = user(username);
            if (Objects.nonNull(roles)) {
                request = request.with(user.roles(roles));
            } else {
                request = request.with(user);
            }
        }

        if (Objects.nonNull(content)) {
            request.contentType(APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(content));
        }

        var resultActions = mockMvc.perform(request).andExpect(status);

        if (Objects.nonNull(redirectedUrl)) {
            resultActions.andExpect(redirectedUrl(redirectedUrl));
        }
        if (Objects.nonNull(redirectedUrlPattern)) {
            resultActions.andExpect(redirectedUrlPattern(redirectedUrlPattern));
        }

        if(Objects.nonNull(returnedObject)){
            resultActions.andExpect(content().json(mapper.writeValueAsString(returnedObject)));
        }

    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete", MockMvcRequestBuilders::delete);
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        var books = getDbBookDTOs();

        return Stream.of(
                            /*number, method, url, username, roles, status, redirectedUrl, redirectedUrlPattern, content, returnedObject*/
                Arguments.of(1, "get", "/book", null, null, status().isFound(), null, "**/login", null, null),
                Arguments.of(2, "get", "/book", "allUser", new String[]{"USER"}, status().isOk(), null, null, null, null),
                Arguments.of(3, "get", "/book", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null, books.subList(0,1)),

                Arguments.of(4, "get","/book/1", "allUser", new String[]{"USER"}, status().isOk(), null, null, null, null),
                Arguments.of(5, "get","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null, null),
                Arguments.of(6, "get","/book/1", "editor2", new String[]{"EDITOR"}, status().isForbidden(), null, null, null, null),

                Arguments.of(7, "post","/book", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, books.get(0), null),
                Arguments.of(8, "post","/book", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, books.get(0), null),

                Arguments.of(9, "put","/book/1", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, books.get(0), null),
                Arguments.of(10, "put","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, books.get(0), null),

                Arguments.of(11, "delete","/book/1", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, null, null),
                Arguments.of(12, "delete","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null, null)
        );
    }

    @Test
    @WithMockUser(username = "editor1", roles = "EDITOR")
    void createEditorAndAccessCheck() throws Exception {
        var books = getDbBooks();
        var book = books.get(0);
        book.setId(0);
        var bookDTO = getDbBookDTOs().get(0);
        bookDTO.setId(null);

        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        String result = mockMvc.perform(post("/book")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        bookDTO = mapper.readValue(result, BookDTO.class);
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



    private static List<Book> getDbBooks() {
        return TestData.getDbBooks();
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

}
