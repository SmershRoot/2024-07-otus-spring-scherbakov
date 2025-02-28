package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthorWithoutBookHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Health health() {
        var authorIds = bookRepository.findAll().stream()
                .map(Book::getAuthor)
                .filter(Objects::nonNull).distinct()
                .map(Author::getId).toList();
        var authorsWithoutBook = authorRepository.findByIdNotIn(authorIds);
        if (authorsWithoutBook.isEmpty()) {
            return Health.up()
                    .withDetail("Authors without book", "No authors without book")
                    .build();
        }

        Map<String, Object> detailMessages = new HashMap<>();
        authorsWithoutBook.forEach(a -> detailMessages.put("Author without book", a.getFullName()));
        return Health.down()
                .withDetails(detailMessages)
                .build();
    }
}
