package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDTO;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDTO> findById(String id);

    List<CommentDTO> findByBookId(String bookId);

    CommentDTO insert(String bookId, String text, String author);

    CommentDTO update(String id, String text);

    void deleteById(String id);

}
