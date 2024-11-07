package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDTO;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDTO> findById(long id);

    List<CommentDTO> findByBookId(long bookId);

    CommentDTO insert(long bookId, String text, String author);

    CommentDTO update(long id, String text);

    void deleteById(long id);

}
