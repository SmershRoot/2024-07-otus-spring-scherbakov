package ru.otus.hw;

import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.h2.CommentJpa;
import ru.otus.hw.models.h2.GenreJpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс для генерации тестовых данных
 */
public class TestData {

    public static List<CommentJpa> getDbComments() {
        var result = new ArrayList<CommentJpa>();
        var dbBooks = getDbBooks().stream().collect(Collectors.toMap(BookJpa::getId, book -> book));

        result.add(new CommentJpa(1, dbBooks.get(1L), "Comment_1_1", LocalDate.parse( "2024-01-01"), "Author_1"));
        result.add(new CommentJpa(2, dbBooks.get(2L), "Comment_2", LocalDate.parse( "2024-01-02"), "Author_2"));
        result.add(new CommentJpa(3, dbBooks.get(3L), "Comment_3", LocalDate.parse( "2024-01-03"), "Author_3"));
        result.add(new CommentJpa(4, dbBooks.get(1L), "Comment_1_2", LocalDate.parse( "2024-01-04"), "Author_1"));
        result.add(new CommentJpa(5, dbBooks.get(1L), "Comment_1_3", LocalDate.parse( "2024-01-04"), "Author_2"));

        return result;
    }

    public static List<BookJpa> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<AuthorJpa> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorJpa(id, "Author_" + id))
                .toList();
    }

    public static List<GenreJpa> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreJpa(id, "Genre_" + id))
                .toList();
    }

    public static List<BookJpa> getDbBooks(List<AuthorJpa> dbAuthors, List<GenreJpa> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookJpa(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

}
