package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.h2.GenreJpa;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.utils.MongoToH2Utils;

import java.util.List;

@Component("bookMapper")
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final MongoToH2Utils utils;

    public BookJpa toJpa(BookMongo book) {
        var result = new BookJpa();
        result.setTitle(book.getTitle());
        result.setAuthor(getAuthor(book.getAuthor()));
        result.setGenres(getGenres(book.getGenres()));

        return result;
    }

    private AuthorJpa getAuthor(AuthorMongo author) {
        var result = authorMapper.toJpa(author);
        result.setId(utils.getAuthorJpaIdByMongoId(author.getId()));
        return result;
    }

    private List<GenreJpa> getGenres(List<GenreMongo> genres) {
        return genres.stream().map(mongo -> {
            var result = genreMapper.toJpa(mongo);
            result.setId(utils.getGenreJpaIdByMongoId(mongo.getId()));
            return result;
        }).toList();
    }

}
