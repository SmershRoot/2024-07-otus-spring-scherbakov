package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.CommentMongo;

import java.util.List;

public interface CommentMongoRepository extends MongoRepository<CommentMongo, String> {

    List<CommentMongo> findAllByBook(BookMongo book);

    void deleteAllByBookId(String bookId);

}
