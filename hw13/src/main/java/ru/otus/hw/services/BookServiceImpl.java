package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
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

    private final BookMapper mapper;

    private final AclServiceBookService aclServiceService;

    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDTO', 'READ')")
    public BookDTO readById(long id) {
        return mapper.toBookDTO(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDTO', 'READ')")
    public Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<BookDTO> readAll() {
        return repository.findAll().stream().map(mapper::toBookDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EDITOR')")
    public BookDTO create(BookDTO view) {
        var entity = mapper.toBook(view);
        entity = repository.save(entity);
        var dto = mapper.toBookDTO(entity);

        aclServiceService.addPermissionForCreate(dto);
        return dto;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDTO', 'WRITE')")
    public BookDTO update(long id, BookDTO view) {
        var entity = findById(id);
        mapper.updateBookFromDto(entity, view);
        entity = repository.save(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDTO', 'DELETE')")
    public void deleteById(long id) {
        var entity = findById(id);
        repository.deleteById(id);
        var dto = mapper.toBookDTO(entity);
        aclServiceService.deleteAllPermission(dto);
    }

}
