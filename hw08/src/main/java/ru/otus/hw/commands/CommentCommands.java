package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cid")
    public String findById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbyid")
    public String findByBookId(String bookId) {
        return commentService.findByBookId(bookId)
                .stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Insert comment", key = "ic")
    public String insert(String bookId, String text, String author) {
        var comment = commentService.insert(bookId, text, author);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String update(String id, String text) {
        var comment = commentService.update(id, text);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void delete(String id) {
        commentService.deleteById(id);
    }
}
