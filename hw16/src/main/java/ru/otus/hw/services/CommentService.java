package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    Comment findById(long id);

    CommentDTO readById(long id);

    List<CommentDTO> readByBookId(long bookId);

    CommentDTO create(long bookId, CommentDTO commentDTO);

    CommentDTO update(long id, CommentDTO commentDTO);

    void deleteById(long id);

}
