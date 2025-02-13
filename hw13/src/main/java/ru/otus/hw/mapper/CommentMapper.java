package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toCommentDTO(Comment entity);

    @Mapping(target = "book", ignore = true)
    Comment toComment(CommentDTO dto);

    @Mapping(target = "book", ignore = true)
    void updateCommentFromDto(@MappingTarget Comment entity, CommentDTO dto);
}
