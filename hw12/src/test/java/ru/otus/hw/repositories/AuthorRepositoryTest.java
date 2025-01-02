package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Author;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Test
    void findAll() {
        var authors = repository.findAll();
        assertNotNull(authors, "Authors is empty");
        var authorMaps = authors.stream().collect(Collectors.toMap(
                Author::getId,
                Function.identity()
        ));

        assertEquals("Author_1", authorMaps.get(1L).getFullName(), "Author_1 is not id 1");
        assertEquals("Author_2", authorMaps.get(2L).getFullName(), "Author_2 is not id 2");
        assertEquals("Author_3", authorMaps.get(3L).getFullName(), "Author_3 is not id 3");

    }

    @Test
    void findById() {
        Optional<Author> author = repository. findById(1L);
        assertTrue(author.isPresent(), "Author is empty");
        assertEquals(1, author.get().getId(), "Author is not id 1");
        assertEquals("Author_1", author.get().getFullName(), "Author_1 is not id 1");
    }
}