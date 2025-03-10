package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    private final BookMapper mapper;

    @Override
    public BookDTO readById(long id) {
        return mapper.toBookDTO(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> readAll() {
        return repository.findAll().stream().map(mapper::toBookDTO).toList();
    }

    @Override
    @Transactional
    public BookDTO create(BookDTO view) {
        var entity = mapper.toBook(view);
        entity = repository.save(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    public BookDTO update(long id, BookDTO view) {
        var entity = findById(id);
        mapper.updateBookFromDto(entity, view);
        entity = repository.save(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

}
