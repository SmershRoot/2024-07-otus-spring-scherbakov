package ru.otus.hw.db.testdata.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

@ChangeLog(order = "002")
public class v002InitData {

    @ChangeSet(order = "004", id = "comments-add-data", author = "scherbakov_a", runAlways = true)
    public void initComments(CommentRepository repository, BookRepository bookRepository) {
        var books = bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getId));

        var comments = new ArrayList<Comment>();
        comments.add(new Comment("1", books.get(0), "Comment_1_1" , LocalDate.of(2024, 1, 1), "Author_1"));
        comments.add(new Comment("2", books.get(1), "Comment_2" , LocalDate.of(2024, 1, 2), "Author_2"));
        comments.add(new Comment("3", books.get(2), "Comment_3" , LocalDate.of(2024, 1, 3), "Author_3"));
        comments.add(new Comment("4", books.get(0), "Comment_1_2" , LocalDate.of(2024, 1, 4), "Author_1"));
        comments.add(new Comment("5", books.get(0), "Comment_1_3" , LocalDate.of(2024, 1, 4), "Author_2"));
        repository.saveAll(comments);
    }

}
