package ru.otus.hw.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.h2.GenreJpaRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class MongoToH2Utils {

    private final AuthorJpaRepository authorJPARepository;

    private final GenreJpaRepository genreJPARepository;

    private final AtomicLong index = new AtomicLong(0);

    private final ConcurrentHashMap<Long, String> authorTempIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> authorMapMonoAndJpaIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, String> genreTempIds = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> genreMapMonoAndJpaIds = new ConcurrentHashMap<>();


    public void h2ClearAll() {
        authorJPARepository.deleteAll();
        genreJPARepository.deleteAll();

        authorTempIds.clear();
        authorMapMonoAndJpaIds.clear();
        genreTempIds.clear();
        genreMapMonoAndJpaIds.clear();
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
        authorMapMonoAndJpaIds.put(mongoId, jpaId);
    }

    public long getAuthorJpaIdByMongoId(String authorId) {
        return authorMapMonoAndJpaIds.get(authorId);
    }


//TODO DELETE
    public Map<Long, String> getAuthorTempIdsForTest() {
        return authorTempIds;
    }

    //TODO DELETE
    public Map<String, Long> getAuthorMapMongoAndJpaIdsForTest() {
        return authorMapMonoAndJpaIds;
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
        genreMapMonoAndJpaIds.put(mongoId, jpaId);
    }

    public long getGenreJpaIdByMongoId(String genreId) {
        return genreMapMonoAndJpaIds.get(genreId);
    }


    //TODO DELETE
    public Map<Long, String> getGenreMapForTest() {
        return genreTempIds;
    }

    //TODO DELETE
    public Map<String, Long> getGenreMapMongoAndJpaIdsForTest() {
        return genreMapMonoAndJpaIds;
    }

}
