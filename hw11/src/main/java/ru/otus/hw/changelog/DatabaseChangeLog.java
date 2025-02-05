package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "000")
public class DatabaseChangeLog {

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "scherbakov_a", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "author-add-data", author = "scherbakov_a", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        for (int i = 1; i <= 3; i++) {
            authors.add(new Author(String.valueOf(i),"Author_" + i));
        }
        repository.saveAll(authors).subscribe();
    }

    @ChangeSet(order = "002", id = "genres-add-data", author = "scherbakov_a", runAlways = true)
    public void initGenres(GenreRepository repository) {
        for (int i = 1; i <= 6; i++) {
            genres.add(new Genre(String.valueOf(i),"Genre_" + i));
        }
        repository.saveAll(genres).subscribe();
    }

    @ChangeSet(order = "003", id = "books-add-data", author = "scherbakov_a", runAlways = true)
    public void initBooks(BookRepository repository) {
        for (int i = 1; i <= 3; i++) {
            books.add(
                    new Book(String.valueOf(i), "BookTitle_" + i, authors.get(i - 1),
                            List.of(genres.get(i * 2 - 2), genres.get(i * 2 - 1))
                    )
            );
        }
        repository.saveAll(books).subscribe();
    }

    @ChangeSet(order = "004", id = "comments-add-data", author = "scherbakov_a", runAlways = true)
    public void initComments(CommentRepository repository) {
        var comments = new ArrayList<Comment>();
        comments.add(new Comment("1", books.get(0), "Comment_1_1" , LocalDate.of(2024, 1, 1), "Author_1"));
        comments.add(new Comment("2", books.get(1), "Comment_2" , LocalDate.of(2024, 1, 2), "Author_2"));
        comments.add(new Comment("3", books.get(2), "Comment_3" , LocalDate.of(2024, 1, 3), "Author_3"));
        comments.add(new Comment("4", books.get(0), "Comment_1_2" , LocalDate.of(2024, 1, 4), "Author_1"));
        comments.add(new Comment("5", books.get(0), "Comment_1_3" , LocalDate.of(2024, 1, 4), "Author_2"));
        repository.saveAll(comments).subscribe();
    }
}
