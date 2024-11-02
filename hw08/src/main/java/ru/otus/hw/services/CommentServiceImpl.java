package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookRepository bookRepository;

    private final CommentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findById(String id) {
        return repository.findById(id).map(mapper::toCommentDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findByBookId(String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return repository.findAllByBook(book).stream().map(mapper::toCommentDTO).toList();
    }

    @Override
    @Transactional
    public CommentDTO insert(String bookId, String text, String author) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Comment comment = new Comment();
        comment.setBook(book);
        comment.setCommentDate(LocalDate.now());
        comment.setText(text);
        comment.setAuthor(author);

        comment = repository.save(comment);
        return mapper.toCommentDTO(comment);
    }

    @Override
    @Transactional
    public CommentDTO update(String id, String text) {
        var comment = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setText(text);
        comment = repository.save(comment);
        return mapper.toCommentDTO(comment);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
