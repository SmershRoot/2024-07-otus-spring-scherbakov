package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.models.Author;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с авторами")
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "mongock.enabled=true")
class AuthorRepositoryTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void findAll() {
        var authors = mongoOperations.findAll(Author.class);
        assertNotNull(authors, "Authors is empty");
        var authorMaps = authors.stream().collect(Collectors.toMap(
                Author::getId,
                Function.identity()
        ));

        assertEquals("Author_1", authorMaps.get("1").getFullName(), "Author_1 is not id 1");
        assertEquals("Author_2", authorMaps.get("2").getFullName(), "Author_2 is not id 2");
        assertEquals("Author_3", authorMaps.get("3").getFullName(), "Author_3 is not id 3");
    }

    @Test
    void findById() {
        Author author = mongoOperations.findById("1", Author.class);
        assertNotNull(author, "Author is empty");
        assertEquals("1", author.getId(), "Author is not id 1");
        assertEquals("Author_1", author.getFullName(), "Author_1 is not id 1");
    }
}