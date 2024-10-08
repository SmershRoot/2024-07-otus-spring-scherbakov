package ru.otus.hw;

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
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
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


}
