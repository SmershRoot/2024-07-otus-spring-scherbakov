package ru.otus.hw.controller.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.TestData;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.configuration.constants.Constants;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.dto.BookBasicDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    public void testHomePageWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status and redirect = {5}")
    @MethodSource("getTestData")
    public void shouldReturnExpectedStatus(String method,
                                           String uri,
                                           String username,
                                           String[] roles,
                                           ResultMatcher status,
                                           String redirectedUrl,
                                           String redirectedUrlPattern) throws Exception {

        var request = method2RequestBuilder(method, uri);

        if (Objects.nonNull(username)) {
            var user = user(username);
            if (Objects.nonNull(roles)) {
                request = request.with(user.roles(roles));
            } else {
                request = request.with(user);
            }
        }

        when(bookService.findBasicById(1)).thenReturn(getBookBasicDTO());

        if("post".equalsIgnoreCase(method)){
            request = request.param("id", "0")
                    .flashAttr("book", getBookBasicDTO());
        }

        var resultActions = mockMvc.perform(request).andExpect(status);

        if (Objects.nonNull(redirectedUrl)) {
            resultActions.andExpect(redirectedUrl(redirectedUrl));
        }
        if (Objects.nonNull(redirectedUrlPattern)) {
            resultActions.andExpect(redirectedUrlPattern(redirectedUrlPattern));
        }

    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        return Stream.of(
                Arguments.of("get", "/books", "allUser", null, status().isOk(), null, null),
                Arguments.of("get", "/books", null, null, status().is3xxRedirection(), null, "**/login"),
                Arguments.of("get", "/books/1", "admin",  new String[]{Constants.Authority.ADMIN}, status().isOk(), null, null),
                Arguments.of("get", "/books/1", "user", null, status().isOk(), null, null),
                Arguments.of("get", "/books/1", null, null, status().is3xxRedirection(), null, "**/login"),
                Arguments.of("get", "/books/0", "admin", new String[]{Constants.Authority.ADMIN}, status().isOk(), null, null),
                Arguments.of("post", "/books/save", "admin", new String[]{Constants.Authority.ADMIN}, status().is3xxRedirection(), "/books", null),
                Arguments.of("post", "/books/save", "user", null, status().is3xxRedirection(), "/forbidden", null),
                Arguments.of("post", "/books/save", null, null, status().is3xxRedirection(), null, "**/login"),
                Arguments.of("post", "/books/delete", "admin", new String[]{Constants.Authority.ADMIN}, status().is3xxRedirection(), "/books", null),
                Arguments.of("post", "/books/delete", "user", null, status().is3xxRedirection(), "/forbidden", null),
                Arguments.of("post", "/books/delete", null, null, status().is3xxRedirection(), null, "**/login")

        );
    }

    private static BookBasicDTO getBookBasicDTO() {
        var result = new BookBasicDTO();
        result.setId(0);
        result.setTitle("");
        result.setAuthorId(0L);
        result.setGenreIds(List.of(0L));
        return result;
    }

}