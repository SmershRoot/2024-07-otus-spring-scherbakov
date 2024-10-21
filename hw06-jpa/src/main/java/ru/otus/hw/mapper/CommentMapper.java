package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toCommentDTO(Comment comment);

}
