package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.CommentMapperImpl;
import ru.otus.hw.models.Comment;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataMongoTest
@Import(value = {
        CommentServiceImpl.class, BookServiceImpl.class,
        CommentMapperImpl.class, BookMapperImpl.class
})
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @ParameterizedTest
    @DisplayName("должен загружать комментарий по id")
    @MethodSource("getDbCommentDTOs")
    void findById(CommentDTO expectedComment) {
        var actualComment = service.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @Test
    void findByBookId() {
        var dbCommentIds = TestData.getDbComments().stream()
                .filter(comment -> comment.getBook().getId().equals("1"))
                .map(Comment::getId)
                .toList();
        var dbComments = getDbCommentDTOs();
        var expectedBookOneComments = dbComments
                .stream()
                .filter(comment -> dbCommentIds.contains(comment.getId()))
                .toList();
        var actualComment = service.findByBookId("1");
        assertThat(actualComment).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedBookOneComments);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insert() {
        var dbBook = TestData.getDbBookDTOs().get(0);

        var expectedComment = new CommentDTO(null, "NEW COMMENT", LocalDate.now(), "AUTHOR_TEST");
        var returnedComment  = service.insert(dbBook.getId(), "NEW COMMENT", "AUTHOR_TEST");
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id", "book")
                .isEqualTo(expectedComment);

        assertThat(service.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);

        var actualComments = service.findByBookId("1");
        assertThat(actualComments).contains(returnedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() {
        var dbBook = TestData.getDbBookDTOs().stream().filter(book -> book.getId().equals("1")).findFirst().get();
        var expectedComment = new CommentDTO("1", "NEW COMMENT", LocalDate.parse("2024-01-01"), "Author_1");

        assertThat(service.findById("1"))
                .isPresent()
                .get()
                .matches(comment -> comment.getId().equals(expectedComment.getId()))
                .matches(comment -> comment.getAuthor().equals(expectedComment.getAuthor()))
                .matches(comment -> comment.getCommentDate().equals(expectedComment.getCommentDate()))
                .matches(comment -> !comment.getText().equals("NEW COMMENT"));

        var returnedComment = service.update(
                expectedComment.getId(),
                expectedComment.getText()
        );

        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);

        assertThat(service.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        assertThat(service.findById("1")).isPresent();
        service.deleteById("1");
        assertThat(service.findById("1")).isEmpty();
    }

    private static List<CommentDTO> getDbCommentDTOs() {
        return TestData.getDbCommentDTOs();
    }

}