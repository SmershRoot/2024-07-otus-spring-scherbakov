package ru.otus.hw.repositories;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.TestData;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataMongoTest
@TestPropertySource(properties = "mongock.enabled=true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    private static List<Book> dbBooks;

    @BeforeAll
    void setUp() {
        dbBooks = mongoOperations.findAll(Book.class);
    }

    @DisplayName("получение всех комментариев по книге")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findAllByBook(Book expectedBook){
        var actualComments = repository.findAllByBookId(expectedBook.getId()).collectList().block();
        var expectedComments = TestData.getDbComments()
                .stream().filter(c -> c.getBook().equals(expectedBook))
                .toList();

        assertThat(actualComments).containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    private static List<Book> getDbBooks() {
        return dbBooks;
    }

}