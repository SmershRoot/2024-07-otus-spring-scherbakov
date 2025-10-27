package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с авторами")
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "mongock.enabled=true")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void findAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(3)
                .verifyComplete();

        var actualAuthors = repository.findAll().collectList().block();
        var expectedAuthors = mongoOperations.findAll(Author.class);
        assertNotNull(actualAuthors, "Authors is empty");

        var authorMaps = actualAuthors.stream().collect(Collectors.toMap(
                Author::getId,
                Function.identity()
        ));
        assertEquals(expectedAuthors.size(), authorMaps.size(), "Authors size is not equal");
        expectedAuthors.forEach(author -> {
            if (authorMaps.containsKey(author.getId())) {
                assertEquals(author.getFullName(), authorMaps.get(author.getId()).getFullName(), "Author FullName is not equal");
            } else {
                fail("Author not found");
            }
        });
    }

    @Test
    void findById() {
        Optional<Author> actualAuthor = repository.findById("1").blockOptional();
        Author expectedAuthor = mongoOperations.findById("1", Author.class);
        assertTrue(actualAuthor.isPresent(), "Author is empty");
        assertEquals("1", actualAuthor.get().getId(), "Author is not id 1");
        assertEquals(expectedAuthor.getFullName(), actualAuthor.get().getFullName(), "Author FullName is not equal for id 1");
    }
}