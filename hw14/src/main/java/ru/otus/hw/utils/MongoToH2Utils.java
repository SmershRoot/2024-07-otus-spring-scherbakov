package ru.otus.hw.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.h2.BookJpaRepository;
import ru.otus.hw.repositories.h2.GenreJpaRepository;
import ru.otus.hw.repositories.h2.CommentJpaRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class MongoToH2Utils {

    private final AuthorJpaRepository authorJPARepository;

    private final GenreJpaRepository genreJPARepository;

    private final BookJpaRepository bookJPARepository;

    private final CommentJpaRepository commentJPARepository;

    private final AtomicLong index = new AtomicLong(0);

    private final ConcurrentHashMap<Long, String> authorTempIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> authorMapMongoAndJpaIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, String> genreTempIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> genreMapMongoAndJpaIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, String> bookTempIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> bookMapMongoAndJpaIds = new ConcurrentHashMap<>();


    public void h2ClearAll() {
        authorJPARepository.deleteAll();
        genreJPARepository.deleteAll();
        bookJPARepository.deleteAll();
        commentJPARepository.deleteAll();

        authorTempIds.clear();
        authorMapMongoAndJpaIds.clear();
        genreTempIds.clear();
        genreMapMongoAndJpaIds.clear();
        bookTempIds.clear();
        bookMapMongoAndJpaIds.clear();
    }

    public synchronized long generateAuthorJpaTempId(String authorId) {
        authorTempIds.put(index.incrementAndGet(), authorId);
        return index.get();
    }

    public String getAuthorMongoIdByJpaTempId(long authorId) {
        return authorTempIds.get(authorId);
    }

    public void addAuthorMongoIdAndJpaId(long tempId, long jpaId) {
        var mongoId = getAuthorMongoIdByJpaTempId(tempId);
        authorMapMongoAndJpaIds.put(mongoId, jpaId);
    }

    public long getAuthorJpaIdByMongoId(String authorId) {
        return authorMapMongoAndJpaIds.get(authorId);
    }

    public synchronized long generateGenreJpaTempId(String genreId) {
        genreTempIds.put(index.incrementAndGet(), genreId);
        return index.get();
    }

    public String getGenreMongoIdByJpaTempId(long genreId) {
        return genreTempIds.get(genreId);
    }

    public void addGenreMongoIdAndJpaId(long tempId, long jpaId) {
        var mongoId = getGenreMongoIdByJpaTempId(tempId);
        genreMapMongoAndJpaIds.put(mongoId, jpaId);
    }

    public long getGenreJpaIdByMongoId(String genreId) {
        return genreMapMongoAndJpaIds.get(genreId);
    }

    public synchronized long generateBookJpaTempId(String bookId) {
        bookTempIds.put(index.incrementAndGet(), bookId);
        return index.get();
    }

    public String getBookMongoIdByJpaTempId(long bookId) {
        return bookTempIds.get(bookId);
    }

    public void addBookMongoIdAndJpaId(long tempId, long jpaId) {
        var mongoId = getBookMongoIdByJpaTempId(tempId);
        bookMapMongoAndJpaIds.put(mongoId, jpaId);
    }

    public long getBookJpaIdByMongoId(String bookId) {
        return bookMapMongoAndJpaIds.get(bookId);
    }

}
