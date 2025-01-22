package ru.otus.hw.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.h2.CommentJpa;

public interface CommentJpaRepository extends JpaRepository<CommentJpa, Long> {
}
