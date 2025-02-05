package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith( SpringExtension.class )
@WebFluxTest(controllers = {GenreController.class})
@Import({GenreRepository.class, GenreMapper.class})
public class GenreControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreRepository repository;

    @MockBean
    private GenreMapper mapper;

    @Test
    public void read() {
        List<GenreDTO> genreDTOs = getDbGenreDTOs();
        List<Genre> genres = getDbGenres();
        when(repository.findAll()).thenReturn(Flux.fromIterable(genres));
        when(mapper.toGenreDTO(genres.get(0))).thenReturn(genreDTOs.get(0));
        when(mapper.toGenreDTO(genres.get(1))).thenReturn(genreDTOs.get(1));
        when(mapper.toGenreDTO(genres.get(2))).thenReturn(genreDTOs.get(2));
        when(mapper.toGenreDTO(genres.get(3))).thenReturn(genreDTOs.get(3));
        when(mapper.toGenreDTO(genres.get(4))).thenReturn(genreDTOs.get(4));
        when(mapper.toGenreDTO(genres.get(5))).thenReturn(genreDTOs.get(5));

        webClient.get().uri("/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDTO.class).isEqualTo(genreDTOs);

        verify(repository, times(1)).findAll();
    }

    private static List<Genre> getDbGenres() {
        return TestData.getDbGenres();
    }

    private static List<GenreDTO> getDbGenreDTOs() {
        return TestData.getDbGenreDTOs();
    }

}
