package ru.otus.hw.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.GenerateData;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @DisplayName("получение всех комментариев по книге")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findAllByBook(Book expectedBook){
        var actualComments = repository.findAllByBook(expectedBook);
        var expectedComments = GenerateData.getDbComments()
                .stream().filter(c -> c.getBook().equals(expectedBook))
                .toList();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    private static List<Book> getDbBooks() {
        return GenerateData.getDbBooks();
    }

}