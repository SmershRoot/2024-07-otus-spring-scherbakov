package ru.otus.hw.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = FunctionalEndpointsBookConfig.class)
@Import({BookRepository.class, BookMapper.class})
@EnableAutoConfiguration
public class FunctionalEndpointsBookRouteTest {

    @Autowired
    private FunctionalEndpointsBookConfig config;

    @MockBean
    private BookRepository repository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookMapper mapper;

    @Test
    void getComposedRoutes_findAll() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.getComposedRoutes(
                        mapper, repository, authorRepository, genreRepository, commentRepository))
                .build();

        List<BookDTO> bookDTOs = getDbBookDTOs();
        List<Book> books = getDbBooks();
        when(repository.findAll()).thenReturn(Flux.fromIterable(books));
        when(mapper.toBookDTO(books.get(0))).thenReturn(bookDTOs.get(0));
        when(mapper.toBookDTO(books.get(1))).thenReturn(bookDTOs.get(1));
        when(mapper.toBookDTO(books.get(2))).thenReturn(bookDTOs.get(2));

        client.get()
                .uri("/library-api/route/book")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDTO.class)
                .isEqualTo(bookDTOs);
    }

    @Test
    void getComposedRoutes_findById() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.getComposedRoutes(
                        mapper, repository, authorRepository, genreRepository, commentRepository))
                .build();

        BookDTO bookDTO = getDbBookDTOs().get(0);
        Book book = getDbBooks().get(0);
        when(repository.findById(book.getId())).thenReturn(Mono.just(book));
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        client.get()
                .uri("/library-api/route/book/"+book.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class)
                .isEqualTo(bookDTO);
    }

    @Test
    void crateRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.crateRoute(
                        mapper, repository, authorRepository, genreRepository, commentRepository))
                .build();
        BookDTO bookDTO = getDbBookDTOs().get(0);
        bookDTO.setId(null);
        Book book = getDbBooks().get(0);
        book.setId(null);
        when(repository.save(book)).thenReturn(Mono.just(book));
        book.getGenres().forEach(g ->
              when(genreRepository.findById(g.getId())).thenReturn(Mono.just(g))
        );
        when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Mono.just(book.getAuthor()));

        when(mapper.toBook(bookDTO)).thenReturn(book);
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        client.post()
                .uri("/library-api/route/book")
                .header("Content-Type", "application/json")
                .body(Mono.just(bookDTO), BookDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class);

        verify(repository).save(book);
    }

    @Test
    void updateRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.updateRoute(
                        mapper, repository, authorRepository, genreRepository, commentRepository))
                .build();
        BookDTO bookDTO = getDbBookDTOs().get(0);
        Book book = getDbBooks().get(0);
        when(repository.findById(book.getId())).thenReturn(Mono.just(book));
        when(repository.save(book)).thenReturn(Mono.just(book));
        when(mapper.toBook(bookDTO)).thenReturn(book);
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        client.put()
                .uri("/library-api/route/book/"+book.getId())
                .body(Mono.just(bookDTO), BookDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class)
                .isEqualTo(bookDTO);

        verify(repository).save(book);
    }

    @Test
    void deleteRoute(){
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.deleteRoute(
                        mapper, repository, authorRepository, genreRepository, commentRepository))
                .build();
        Book book = getDbBooks().get(0);
        when(repository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.deleteAllByBookId(book.getId())).thenReturn(Mono.empty().then());
        when(repository.deleteById(book.getId())).thenReturn(Mono.empty().then());

        client.delete()
                .uri("/library-api/route/book/"+book.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository).deleteById(book.getId());
    }


    private static List<Book> getDbBooks() {
        return TestData.getDbBooks();
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

}
