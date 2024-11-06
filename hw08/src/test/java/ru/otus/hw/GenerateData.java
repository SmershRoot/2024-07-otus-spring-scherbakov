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

/**
 * Класс для генерации тестовых данных
 */
public class GenerateData {

    public static List<Comment> getDbComments() {
        var result = new ArrayList<Comment>();
        var dbBooks = getDbBooks().stream().collect(Collectors.toMap(Book::getId, book -> book));

        result.add(new Comment("1", dbBooks.get("1"), "Comment_1_1", LocalDate.parse( "2024-01-01"), "Author_1"));
        result.add(new Comment("2", dbBooks.get("2"), "Comment_2", LocalDate.parse( "2024-01-02"), "Author_2"));
        result.add(new Comment("3", dbBooks.get("3"), "Comment_3", LocalDate.parse( "2024-01-03"), "Author_3"));
        result.add(new Comment("4", dbBooks.get("1"), "Comment_1_2", LocalDate.parse( "2024-01-04"), "Author_1"));
        result.add(new Comment("5", dbBooks.get("1"), "Comment_1_3", LocalDate.parse( "2024-01-04"), "Author_2"));

        return result;
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.valueOf(id), "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(String.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDTO> getDbCommentDTOs() {
        var result = new ArrayList<CommentDTO>();

        result.add(new CommentDTO("1", "Comment_1_1", LocalDate.parse( "2024-01-01"), "Author_1"));
        result.add(new CommentDTO("2", "Comment_2", LocalDate.parse( "2024-01-02"), "Author_2"));
        result.add(new CommentDTO("3", "Comment_3", LocalDate.parse( "2024-01-03"), "Author_3"));
        result.add(new CommentDTO("4", "Comment_1_2", LocalDate.parse( "2024-01-04"), "Author_1"));
        result.add(new CommentDTO("5", "Comment_1_3", LocalDate.parse( "2024-01-04"), "Author_2"));

        return result;
    }

    public static List<BookDTO> getDbBookDTOs() {
        var dbAuthors = getDbAuthorDTOs();
        var dbGenres = getDbGenreDTOs();
        return getDbBookDTOs(dbAuthors, dbGenres);
    }

    public static List<AuthorDTO> getDbAuthorDTOs() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDTO(String.valueOf(id), "Author_" + id))
                .toList();
    }

    public static List<GenreDTO> getDbGenreDTOs() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDTO(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    public static List<BookDTO> getDbBookDTOs(List<AuthorDTO> dbAuthors, List<GenreDTO> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDTO(String.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

}
