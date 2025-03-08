package ru.otus.hw.controllers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.TestData;
import ru.otus.hw.configuration.security.SecurityConfig;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class SecurityBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

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
            BookDTO content) throws Exception {
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
                Arguments.of(1, "get", "/book", null, null, status().isFound(), null, "**/login", null),
                Arguments.of(2, "get", "/book", "allUser", new String[]{"USER"}, status().isOk(), null, null, null),
                Arguments.of(3, "get", "/book", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null),

                Arguments.of(4, "get","/book/1", "allUser", new String[]{"USER"}, status().isOk(), null, null, null),
                Arguments.of(5, "get","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null),

                Arguments.of(6, "post","/book", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, books.get(0)),
                Arguments.of(7, "post","/book", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, books.get(0)),

                Arguments.of(8, "put","/book/1", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, books.get(0)),
                Arguments.of(9, "put","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, books.get(0)),

                Arguments.of(10, "delete","/book/1", "allUser", new String[]{"USER"}, status().isForbidden(), null, null, null),
                Arguments.of(19, "delete","/book/1", "editor1", new String[]{"EDITOR"}, status().isOk(), null, null, null)
        );
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

}
