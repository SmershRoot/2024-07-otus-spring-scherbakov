package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class,
        JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @DisplayName("должен загружать книгу по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findById(Book expectedBook) {
        var actualBook = service.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);


        assertDoesNotThrow(() -> actualBook.get().getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBook.get().getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAll() {
        var actualBooks = service.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
        assertDoesNotThrow(() -> actualBooks.get(0).getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBooks.get(0).getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insert() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = service.insert(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(service.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = service.update(
                expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        assertThat(service.findById(1L)).isPresent();
        service.deleteById(1L);
        assertThat(service.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}