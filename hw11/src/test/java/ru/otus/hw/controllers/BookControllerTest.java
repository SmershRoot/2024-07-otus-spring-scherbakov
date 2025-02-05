package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import reactor.test.StepVerifier;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/*
Для себя оставлю комментарий:
По описанию на гите mongock существует проблема при инициализации контекста приложения:
Unsatisfied dependency expressed through method 'connectionDriver' parameter 0:
No qualifying bean of type 'org.springframework.data.mongodb.core.MongoTemplate' available:
expected at least 1 bean which qualifies as autowire candidate. Dependency annotations

Разработчик о проблеме знает, но у него своей работы много и он предлагает кому-нибудь
не равнодушному к продукту, исправить это и выложить PR.
А пока решение @TestPropertySource(properties = ["mongock.enabled=false"])

https://github.com/mongock/mongock/issues/239
 */
@ExtendWith( SpringExtension.class )
@WebFluxTest(controllers = {BookController.class})
public class BookControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository repository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookMapper mapper;

    @Test
    void read() {
        List<BookDTO> bookDTOs = getDbBookDTOs();
        List<Book> books = getDbBooks();

        when(repository.findAll()).thenReturn(Flux.fromIterable(books));
        when(mapper.toBookDTO(books.get(0))).thenReturn(bookDTOs.get(0));
        when(mapper.toBookDTO(books.get(1))).thenReturn(bookDTOs.get(1));
        when(mapper.toBookDTO(books.get(2))).thenReturn(bookDTOs.get(2));

        Flux<BookDTO> result = webClient.get().uri("/book")
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDTO.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDTO> stepResult = null;
        for (var dto : bookDTOs) {
            stepResult = step.expectNext(dto);
        }
        assert stepResult != null;
        stepResult.verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        BookDTO bookDTO = getDbBookDTOs().get(0);
        Book book = getDbBooks().get(0);

        when(repository.findById(bookDTO.getId())).thenReturn(Mono.just(book));
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = webClient.get().uri("/book/{id}", bookDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDTO.class)
                .getResponseBody().blockFirst();

        assertThat(result).isEqualTo(bookDTO);
    }

    @Test
    void findByIdException() {
        when(repository.findById("test")).thenReturn(Mono.empty());

       webClient.get().uri("/book/{id}", "test")
               .exchange()
               .expectStatus().is5xxServerError();
    }

    @Test
    void crate() {
        BookDTO bookDTO = getDbBookDTOs().get(0);
        bookDTO.setId(null);
        Book book = getDbBooks().get(0);
        book.setId(null);
        when(repository.save(book)).thenReturn(Mono.just(book));
        when(mapper.toBook(bookDTO)).thenReturn(book);
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        var result = webClient.post()
                .uri("/book")
                .bodyValue(bookDTO)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookDTO.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(bookDTO)
                .verifyComplete();

        verify(repository).save(book);
    }

    @Test
    void update() {
        BookDTO bookDTO = getDbBookDTOs().get(0);
        Book book = getDbBooks().get(0);
        when(repository.findById(book.getId())).thenReturn(Mono.just(book));
        when(repository.save(book)).thenReturn(Mono.just(book));
        when(mapper.toBook(bookDTO)).thenReturn(book);
        when(mapper.toBookDTO(book)).thenReturn(bookDTO);

        webClient.put()
                .uri("/book/"+book.getId())
                .body(Mono.just(bookDTO), BookDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class)
                .isEqualTo(bookDTO);

        verify(repository).save(book);
    }

    @Test
    void delete(){
        Book book = getDbBooks().get(0);
        when(repository.findById(book.getId())).thenReturn(Mono.just(book));
        when(repository.deleteById(book.getId())).thenReturn(Mono.empty().then());
        when(commentRepository.deleteAllByBookId(book.getId())).thenReturn(Mono.empty().then());

        webClient.delete()
                .uri("/book/"+book.getId())
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
