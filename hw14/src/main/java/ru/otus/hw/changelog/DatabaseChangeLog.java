package ru.otus.hw.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.repositories.mongo.AuthorMongoRepository;
import ru.otus.hw.repositories.mongo.BookMongoRepository;
import ru.otus.hw.repositories.mongo.CommentMongoRepository;
import ru.otus.hw.repositories.mongo.GenreMongoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "000")
public class DatabaseChangeLog {

    private final List<AuthorMongo> authors = new ArrayList<>();

    private final List<GenreMongo> genres = new ArrayList<>();

    private final List<BookMongo> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "scherbakov_a", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "author-add-data", author = "scherbakov_a", runAlways = true)
    public void initAuthors(AuthorMongoRepository repository) {
        for (int i = 1; i <= 30; i++) {
            authors.add(new AuthorMongo(String.valueOf(i),"Author_" + i));
        }
        repository.saveAll(authors);
    }

    @ChangeSet(order = "002", id = "genres-add-data", author = "scherbakov_a", runAlways = true)
    public void initGenres(GenreMongoRepository repository) {
        for (int i = 1; i <= 6; i++) {
            genres.add(new GenreMongo(String.valueOf(i),"Genre_" + i));
        }
        repository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "books-add-data", author = "scherbakov_a", runAlways = true)
    public void initBooks(BookMongoRepository repository) {
        for (int i = 1; i <= 3; i++) {
            books.add(
                    new BookMongo(String.valueOf(i), "BookTitle_" + i, authors.get(i - 1),
                            List.of(genres.get(i * 2 - 2), genres.get(i * 2 - 1))
                    )
            );
        }
        repository.saveAll(books);
    }

    @ChangeSet(order = "004", id = "comments-add-data", author = "scherbakov_a", runAlways = true)
    public void initComments(CommentMongoRepository repository) {
        var comments = new ArrayList<CommentMongo>();
        comments.add(new CommentMongo("1", books.get(0), "Comment_1_1" , LocalDate.of(2024, 1, 1), "Author_1"));
        comments.add(new CommentMongo("2", books.get(1), "Comment_2" , LocalDate.of(2024, 1, 2), "Author_2"));
        comments.add(new CommentMongo("3", books.get(2), "Comment_3" , LocalDate.of(2024, 1, 3), "Author_3"));
        comments.add(new CommentMongo("4", books.get(0), "Comment_1_2" , LocalDate.of(2024, 1, 4), "Author_1"));
        comments.add(new CommentMongo("5", books.get(0), "Comment_1_3" , LocalDate.of(2024, 1, 4), "Author_2"));
        repository.saveAll(comments);
    }
}
