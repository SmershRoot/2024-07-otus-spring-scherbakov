package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(
                em.find(Book.class, id,
                        Map.of("jakarta.persistence.fetchgraph",getGraph())
                )
        );
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class)
                .setHint("jakarta.persistence.fetchgraph",
                        getGraph())
                .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    public EntityGraph<?> getGraph() {
        var graph = em.createEntityGraph(Book.class);
        graph.addAttributeNodes("author");
        return graph;
    }

}
