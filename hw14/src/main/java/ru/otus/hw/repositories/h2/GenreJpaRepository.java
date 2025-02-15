package ru.otus.hw.repositories.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.h2.GenreJpa;

public interface GenreJpaRepository extends JpaRepository<GenreJpa, Long> {
}
