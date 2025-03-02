package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookService bookService;

    private final CommentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Comment findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDTO readById(long id) {
        return mapper.toCommentDTO(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> readByBookId(long bookId) {
        var book = bookService.findById(bookId);
        return repository.findAllByBook(book).stream().map(mapper::toCommentDTO).toList();
    }

    @Override
    @Transactional
    public CommentDTO create(long bookId, CommentDTO view) {
        var entity = mapper.toComment(view);
        entity.setBook(bookService.findById(bookId));
        entity = repository.save(entity);
        return mapper.toCommentDTO(entity);
    }

    @Override
    @Transactional
    public CommentDTO update(long id, CommentDTO view) {
        var entity = findById(id);
        mapper.updateCommentFromDto(entity, view);
        entity = repository.save(entity);
        return mapper.toCommentDTO(entity);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
