package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookService bookService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(long bookId) {
        return repository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(long bookId, String text, String author) {
        var book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Comment comment = new Comment();
        comment.setBook(book);
        comment.setCommentDate(LocalDate.now());
        comment.setText(text);
        comment.setAuthor(author);

        return repository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        var comment = findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setText(text);
        return repository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
