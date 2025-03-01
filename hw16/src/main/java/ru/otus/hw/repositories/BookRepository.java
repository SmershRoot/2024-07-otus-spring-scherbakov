package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookAuthorProjection;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book", excerptProjection = BookAuthorProjection.class)
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"author"})
    List<Book> findAll();

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"author", "genres"})
    Optional<Book> findById(Long id);
}
