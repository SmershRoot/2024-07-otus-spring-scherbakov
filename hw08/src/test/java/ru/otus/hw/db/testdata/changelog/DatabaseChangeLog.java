package ru.otus.hw.db.testdata.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "000")
public class DatabaseChangeLog {

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "scherbakov_a", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "author-add-data", author = "scherbakov_a", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        for (int i = 1; i <= 3; i++) {
            authors.add(new Author(String.valueOf(i),"Author_" + i));
        }
        repository.saveAll(authors);
    }

    @ChangeSet(order = "002", id = "genres-add-data", author = "scherbakov_a", runAlways = true)
    public void initGenres(GenreRepository repository) {
        for (int i = 1; i <= 6; i++) {
            genres.add(new Genre(String.valueOf(i),"Genre_" + i));
        }
        repository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "books-add-data", author = "scherbakov_a", runAlways = true)
    public void initBooks(BookRepository repository) {
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            books.add(
                    new Book(String.valueOf(i), "BookTitle_" + i, authors.get(i - 1),
                            List.of(genres.get(i * 2 - 2), genres.get(i * 2 - 1))
                    )
            );
        }
        repository.saveAll(books);
    }

}
