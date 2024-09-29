package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        var params = Map.of("id", id);
        var book = namedParameterJdbcTemplate.query(
                "SELECT b.id, b.title, " +
                        "a.id author_id, a.full_name author_name, " +
                        "g.id genre_id, g.name genre_name " +
                     "FROM books b " +
                        "LEFT JOIN authors a ON b.author_id = a.id " +
                        "LEFT JOIN books_genres bg ON b.id = bg.book_id " +
                        "LEFT JOIN genres g ON bg.genre_id = g.id " +
                     "WHERE b.id = :id",
                params,
                new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var params = Map.of("id", id);
        namedParameterJdbcTemplate.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcTemplate.query(
                "SELECT b.id, b.title, " +
                        "a.id author_id, a.full_name author_name, " +
                        "FROM books b " +
                        "JOIN authors a ON b.author_id = a.id ",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcTemplate.query(
                "select bg.book_id, bg.genre_id from books_genres as bg",
                new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        var booksMap = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        var genresMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));
        var groupRelations = relations.stream()
                .collect(Collectors.groupingBy(
                        BookGenreRelation::bookId,
                        Collectors.mapping(r -> genresMap.get(r.genreId), Collectors.toList()))
                );

        groupRelations.forEach((bookId, listRelations) -> {
            booksMap.get(bookId).getGenres().addAll(listRelations);
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource(
                Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId())
        );
        namedParameterJdbcTemplate.update(
                "insert into books (title, author_id) values (:title, :authorId)",
                params, keyHolder, new String[]{"id"}
        );

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = Map.of("id", book.getId(), "title", book.getTitle(), "authorId", book.getAuthor().getId());

        int count = namedParameterJdbcTemplate.update(
                "update books set title = :title, author_id = :authorId where id = :id",
                params
        );
        if (count == 0) {
            throw new EntityNotFoundException("Book not found");
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
       var params = book.getGenres().stream()
                .map(genre -> Map.of("bookId", book.getId(), "genreId", genre.getId()))
                .toArray(Map[]::new);

        namedParameterJdbcTemplate.batchUpdate(
                "insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
                params
        );
    }

    private void removeGenresRelationsFor(Book book) {
        var params = Map.of("bookId", book.getId());
        namedParameterJdbcTemplate.update("delete from books_genres where book_id = :bookId", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            var author = new Author(
                    rs.getLong("author_id"),
                    rs.getString("author_name")
            );
            book.setAuthor(author);
            book.setGenres(new ArrayList<>());
            return book;
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }
            Book book = new BookRowMapper().mapRow(rs, rs.getRow());
            do {
                var genre = new Genre(
                        rs.getLong("genre_id"),
                        rs.getString("genre_name")
                );
                book.getGenres().add(genre);
            } while (rs.next());
            return book;
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
