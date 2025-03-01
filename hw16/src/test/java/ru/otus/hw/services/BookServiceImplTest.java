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
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapperImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({
        BookServiceImpl.class,
        BookMapperImpl.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @ParameterizedTest
    @DisplayName("должен загружать книгу по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @MethodSource("getDbBookDTOs")
    void findById(BookDTO expectedBook) {
        var actualBook = service.readById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);

        assertDoesNotThrow(() -> actualBook.getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBook.getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAll() {
        var actualBooks = service.readAll();
        var expectedBooks = getDbBookDTOs();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
        assertDoesNotThrow(() -> actualBooks.get(0).getAuthor().getFullName(), "Ошибка загрузки автора");
        assertDoesNotThrow(() -> actualBooks.get(0).getGenres().get(0).getName(), "Ошибка загрузки жанра");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void create() {
        var dbAuthors = TestData.getDbAuthorDTOs();
        var dbGenres = TestData.getDbGenreDTOs();
        var expectedBook = new BookDTO(null, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = service.create(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(service.readById(returnedBook.getId())).isEqualTo(returnedBook);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() {
        var dbAuthors = TestData.getDbAuthorDTOs();
        var dbGenres = TestData.getDbGenreDTOs();
        var expectedBook = new BookDTO(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(service.readById(expectedBook.getId()))
                .isNotEqualTo(expectedBook);

        var returnedBook = service.update(expectedBook.getId(), expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(service.readById(returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        assertNotNull(service.findById(1L));
        service.deleteById(1L);
        var thrown = assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
        assertEquals("Book with id %s not found".formatted(1L), thrown.getMessage());
    }

    private static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }
}