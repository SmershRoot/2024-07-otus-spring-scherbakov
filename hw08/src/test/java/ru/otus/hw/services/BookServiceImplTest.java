package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.GenerateData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.mapper.BookMapperImpl;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами")
@DataMongoTest
@Import({
        BookServiceImpl.class,
        BookMapperImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @ParameterizedTest
    @DisplayName("должен загружать книгу по id")
    @MethodSource("getDbBookDTOs")
    void findById(BookDTO expectedBook) {
        var actualBook = service.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);

        assertDoesNotThrow(() -> actualBook.get().getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBook.get().getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    void findAll() {
        var actualBooks = service.findAll();
        var expectedBooks = getDbBookDTOs();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
        assertDoesNotThrow(() -> actualBooks.get(0).getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBooks.get(0).getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    void insert() {
        var dbAuthors = GenerateData.getDbAuthorDTOs();
        var dbGenres = GenerateData.getDbGenreDTOs();
        var expectedBook = new BookDTO(null, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = service.insert(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet()));
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    void update() {
        var dbAuthors = GenerateData.getDbAuthorDTOs();
        var dbGenres = GenerateData.getDbGenreDTOs();
        var expectedBook = new BookDTO("1", "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(service.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = service.update(
                expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())
        );

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    void deleteById() {
        assertThat(service.findById("1")).isPresent();
        service.deleteById("1");
        assertThat(service.findById("1")).isEmpty();
    }

    private static List<BookDTO> getDbBookDTOs() {
        return GenerateData.getDbBookDTOs();
    }
}