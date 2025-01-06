package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AuthorController.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({AuthorRepository.class, AuthorMapper.class})
@EnableAutoConfiguration
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private AuthorMapper mapper;

    @Test
    public void read() {
        List<AuthorDTO> authorDTOs = getDbAuthorDTOs();
        List<Author> authors = getDbAuthors();
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authors));
        when(mapper.toAuthorDTO(authors.get(0))).thenReturn(authorDTOs.get(0));
        when(mapper.toAuthorDTO(authors.get(1))).thenReturn(authorDTOs.get(1));
        when(mapper.toAuthorDTO(authors.get(2))).thenReturn(authorDTOs.get(2));

        webClient.get().uri("/author")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDTO.class).isEqualTo(authorDTOs);

        verify(authorRepository, times(1)).findAll();
    }

    private static List<Author> getDbAuthors() {
        return TestData.getDbAuthors();
    }

    private static List<AuthorDTO> getDbAuthorDTOs() {
        return TestData.getDbAuthorDTOs();
    }

}
