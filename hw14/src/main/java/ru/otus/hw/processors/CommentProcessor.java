package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.h2.CommentJpa;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.utils.MongoToH2Utils;

@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<CommentMongo, CommentJpa> {

    private final CommentMapper mapper;

    private final MongoToH2Utils utils;

    @Override
    public CommentJpa process(@NonNull CommentMongo commentMongo) throws Exception {
        var commentJpa = mapper.toJpa(commentMongo);
        var bookJpa = new BookJpa();
        bookJpa.setId(utils.getBookJpaIdByMongoId(commentMongo.getBook().getId()));
        commentJpa.setBook(bookJpa);
        return commentJpa;
    }

}
