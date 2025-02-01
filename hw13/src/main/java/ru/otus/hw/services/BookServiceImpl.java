package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    private final BookWithSecurityService securityService;

    private final BookMapper mapper;

    private final AclServiceBookService aclServiceService;

    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'READ')")
    public BookDTO readById(long id) {
        return mapper.toBookDTO(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'READ')")
    public Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> readAll() {
        return securityService.readAll().stream()
                .map(mapper::toBookDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> mapAll(List<Book> books) {
        return books.stream().map(mapper::toBookDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EDITOR')")
    public BookDTO create(BookDTO view) {
        var entity = mapper.toBook(view);
        entity = repository.save(entity);
        var dto = mapper.toBookDTO(entity);

        aclServiceService.addPermissionForCreate(entity);
        return dto;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'WRITE')")
    public BookDTO update(long id, BookDTO view) {
        var entity = findById(id);
        mapper.updateBookFromDto(entity, view);
        entity = repository.save(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE')")
    public void deleteById(long id) {
        var entity = findById(id);
        repository.deleteById(id);
        aclServiceService.deleteAllPermission(entity);
    }

}
