package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.GenerateData;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @ParameterizedTest
    @DisplayName("должен загружать комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @MethodSource("getDbComments")
    void findById(Comment expectedComment) {
        var actualComment = service.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
        assertThat(actualComment.get().getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findByBookId() {
        var dbComments = getDbComments();
        var expectedBookOneComments = dbComments.stream().filter(comment -> comment.getBook().getId() == 1).toList();
        var actualComment = service.findByBookId(1L);
        assertThat(actualComment).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedBookOneComments);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insert() {
        var dbBook = GenerateData.getDbBooks().get(0);

        var expectedComment = new Comment(0, dbBook, "NEW COMMENT", LocalDate.now(), "AUTHOR_TEST");
        var returnedComment  = service.insert(dbBook.getId(), "NEW COMMENT", "AUTHOR_TEST");
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id", "book")
                .isEqualTo(expectedComment);
        assertThat(returnedComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId());

        assertThat(service.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);

        var actualComments = service.findByBookId(1L);
        assertThat(actualComments).contains(returnedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() {
        var dbBook = GenerateData.getDbBooks().stream().filter(book -> book.getId() == 1).findFirst().get();
        var expectedComment = new Comment(1, dbBook, "NEW COMMENT", LocalDate.parse("2024-01-01"), "Author_1");

        assertThat(service.findById(1))
                .isPresent()
                .get()
                .matches(comment -> comment.getId() == expectedComment.getId())
                .matches(comment -> comment.getBook().getId() == expectedComment.getBook().getId())
                .matches(comment -> comment.getAuthor().equals(expectedComment.getAuthor()))
                .matches(comment -> comment.getCommentDate().equals(expectedComment.getCommentDate()))
                .matches(comment -> !comment.getText().equals("NEW COMMENT"));

        var returnedComment = service.update(
                expectedComment.getId(),
                expectedComment.getText()
        );

        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
        assertThat(returnedComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId());

        assertThat(service.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        assertThat(service.findById(1L)).isPresent();
        service.deleteById(1L);
        assertThat(service.findById(1L)).isEmpty();
    }

    private static List<Comment> getDbComments() {
        return GenerateData.getDbComments();
    }

}