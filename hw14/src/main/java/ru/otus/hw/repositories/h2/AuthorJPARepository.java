package ru.otus.hw.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.h2.AuthorJpa;

public interface AuthorJPARepository extends JpaRepository<AuthorJpa, Long> {
}
