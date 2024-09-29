package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcTemplate.query("SELECT id, full_name FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var params = Map.of("id", id);
        var authors = namedParameterJdbcTemplate.query(
                "SELECT id, full_name FROM authors WHERE id = :id",
                params, new AuthorRowMapper()
        );

        if (authors.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(authors.size());
        }

        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var fullName = rs.getString("full_name");

            return new Author(id, fullName);
        }
    }
}
