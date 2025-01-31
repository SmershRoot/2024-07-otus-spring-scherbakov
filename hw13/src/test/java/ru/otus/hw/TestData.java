package ru.otus.hw;

import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Класс для генерации тестовых данных
 */
public class TestData {

    public static List<Comment> getDbComments() {
        var result = new ArrayList<Comment>();
        var dbBooks = getDbBooks().stream().collect(Collectors.toMap(Book::getId, book -> book));

        result.add(new Comment(1, dbBooks.get(1L), "Comment_1_1", LocalDate.parse( "2024-01-01"), "Author_1"));
        result.add(new Comment(2, dbBooks.get(2L), "Comment_2", LocalDate.parse( "2024-01-02"), "Author_2"));
        result.add(new Comment(3, dbBooks.get(3L), "Comment_3", LocalDate.parse( "2024-01-03"), "Author_3"));
        result.add(new Comment(4, dbBooks.get(1L), "Comment_1_2", LocalDate.parse( "2024-01-04"), "Author_1"));
        result.add(new Comment(5, dbBooks.get(1L), "Comment_1_3", LocalDate.parse( "2024-01-04"), "Author_2"));

        return result;
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return new ArrayList<Genre>(IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList());
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDTO> getDbCommentDTOs() {
        var result = new ArrayList<CommentDTO>();

        result.add(new CommentDTO(1L, "Comment_1_1", LocalDate.parse( "2024-01-01"), "Author_1"));
        result.add(new CommentDTO(2L, "Comment_2", LocalDate.parse( "2024-01-02"), "Author_2"));
        result.add(new CommentDTO(3L, "Comment_3", LocalDate.parse( "2024-01-03"), "Author_3"));
        result.add(new CommentDTO(4L, "Comment_1_2", LocalDate.parse( "2024-01-04"), "Author_1"));
        result.add(new CommentDTO(5L, "Comment_1_3", LocalDate.parse( "2024-01-04"), "Author_2"));

        return result;
    }

    public static List<BookDTO> getDbBookDTOs() {
        var dbAuthors = getDbAuthorDTOs();
        var dbGenres = getDbGenreDTOs();
        return getDbBookDTOs(dbAuthors, dbGenres);
    }

    public static List<AuthorDTO> getDbAuthorDTOs() {
        return LongStream.range(1, 4).boxed()
                .map(id -> new AuthorDTO(id, "Author_" + id))
                .toList();
    }

    public static List<GenreDTO> getDbGenreDTOs() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new GenreDTO(id, "Genre_" + id))
                .toList();
    }

    public static List<BookDTO> getDbBookDTOs(List<AuthorDTO> dbAuthors, List<GenreDTO> dbGenres) {
        return LongStream.range(1, 4).boxed()
                .map(id -> new BookDTO(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id.intValue() - 1),
                        dbGenres.subList((id.intValue() - 1) * 2, (id.intValue() - 1) * 2 + 2)
                ))
                .toList();
    }

}
