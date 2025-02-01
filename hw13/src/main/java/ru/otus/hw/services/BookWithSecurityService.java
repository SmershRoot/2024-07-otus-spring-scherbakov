package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookWithSecurityService {

    private final BookRepository repository;

    private final AclServiceBookService aclServiceService;

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'READ')")
    protected Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    protected List<Book> readAll() {
        return repository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EDITOR')")
    protected Book create(Book entity) {
        entity = repository.save(entity);
        aclServiceService.addPermissionForCreate(entity);
        return entity;
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'WRITE')")
    protected Book update(Book entity) {
        return repository.save(entity);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE')")
    protected void deleteById(long id) {
        var entity = findById(id);
        repository.deleteById(id);
        aclServiceService.deleteAllPermission(entity);
    }

}
