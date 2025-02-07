package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.SystemUser;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    SystemUser findByUsername(String username);
}
