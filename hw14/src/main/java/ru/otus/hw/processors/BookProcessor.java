package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.utils.MongoToH2Utils;

@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<BookMongo, BookJpa> {

    private final BookMapper mapper;

    private final MongoToH2Utils utils;

    @Override
    public BookJpa process(@NonNull BookMongo bookMongo) throws Exception {
        var bookJpa = mapper.toJpa(bookMongo);
        //utils.getAuthorJpaIdMyMongoId()
        return bookJpa;
    }
}
