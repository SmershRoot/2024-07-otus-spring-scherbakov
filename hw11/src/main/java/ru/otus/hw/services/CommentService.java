package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    Comment findById(String id);

    CommentDTO readById(String id);

    List<CommentDTO> readByBookId(String bookId);

    CommentDTO create(String bookId, CommentDTO commentDTO);

    CommentDTO update(String id, CommentDTO commentDTO);

    void deleteById(String id);

}
