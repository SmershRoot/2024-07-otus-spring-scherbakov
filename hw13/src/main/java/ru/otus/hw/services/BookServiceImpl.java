package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookWithSecurityService securityService;

    private final BookMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BookDTO readById(long id) {
        return mapper.toBookDTO(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return securityService.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> readAll() {
        return securityService.readAll().stream()
                .map(mapper::toBookDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO create(BookDTO view) {
        var entity = mapper.toBook(view);
        entity = securityService.create(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    public BookDTO update(long id, BookDTO view) {
        var entity = findById(id);
        mapper.updateBookFromDto(entity, view);
        entity = securityService.update(entity);
        return mapper.toBookDTO(entity);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        securityService.deleteById(id);
    }

}
