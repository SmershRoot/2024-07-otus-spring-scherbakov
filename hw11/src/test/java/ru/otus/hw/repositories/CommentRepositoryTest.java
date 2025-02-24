package ru.otus.hw.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.TestData;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataMongoTest
@TestPropertySource(properties = "mongock.enabled=true")
class CommentRepositoryTest {

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("получение всех комментариев по книге")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findAllByBook(Book expectedBook){
        var query = new Query(Criteria.where("book.id").is(expectedBook.getId()));
        var actualComments = mongoOperations.find(query, Comment.class);

        var expectedComments = TestData.getDbComments()
                .stream().filter(c -> c.getBook().equals(expectedBook))
                .toList();

        assertThat(actualComments).containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    private static List<Book> getDbBooks() {
        return TestData.getDbBooks();
    }

}