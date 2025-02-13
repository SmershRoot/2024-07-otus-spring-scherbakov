package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentWithSecurityService {

    private final CommentRepository repository;

    private final AclServiceCommentService aclServiceService;

    @PostAuthorize("hasPermission(returnObject.book.id, 'ru.otus.hw.models.Book', 'READ')")
    public Comment findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @PreAuthorize("hasPermission(#book.id, 'ru.otus.hw.models.Book', 'READ')")
    public List<Comment> readByBook(Book book) {
        return repository.findAllByBook(book);
    }

    @PreAuthorize("hasPermission(#entity.book.id, 'ru.otus.hw.models.Book', 'READ')")
    public Comment create(Comment entity) {
        entity = repository.save(entity);
        aclServiceService.addPermissionForCreate(entity, entity.getBook());
        return entity;
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Comment', 'WRITE')")
    public Comment update(Comment entity) {
        return repository.save(entity);
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Comment', 'DELETE')")
    public void deleteById(long id) {
        var entity = findById(id);
        repository.deleteById(id);
        aclServiceService.deleteAllPermission(entity);
    }
}
