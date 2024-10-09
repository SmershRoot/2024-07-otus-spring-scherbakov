package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"author", "genres"})
    List<Book> findAll();

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"author", "genres"})
    Optional<Book> findById(@NotNull(message = "Book id is null") @Nonnull Long id);
}
