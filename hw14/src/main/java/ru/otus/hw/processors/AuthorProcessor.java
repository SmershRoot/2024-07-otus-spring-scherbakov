package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;

import ru.otus.hw.models.mongo.AuthorMongo;

public class AuthorProcessor implements ItemProcessor<AuthorMongo, AuthorMongo> {

    @Override
    public AuthorMongo process(AuthorMongo author) throws Exception {
        System.out.println("AuthorProcessor.process(): " + author.getId() + " " + author.getFullName());
        //ТУТ

        return author;
    }

}
