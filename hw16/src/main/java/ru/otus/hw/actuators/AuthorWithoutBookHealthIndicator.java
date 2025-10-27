package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthorWithoutBookHealthIndicator implements HealthIndicator {

    private final AuthorService authorService;

    @Override
    public Health health() {
        var authorsWithoutBook = authorService.findAllWithoutBooks();

        if (authorsWithoutBook.isEmpty()) {
            return Health.up()
                    .withDetail("Authors without book", "No authors without book")
                    .build();
        }

        Map<String, Object> detailMessages = new HashMap<>();
        authorsWithoutBook.forEach(a ->
                detailMessages.put("Author without book", a.getFullName()));
        return Health.down()
                .withDetails(detailMessages)
                .build();
    }
}
