package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.lang.NonNull;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.utils.MongoToH2Utils;

@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<AuthorMongo, AuthorJpa> {

    private final AuthorMapper mapper;

    private final MongoToH2Utils utils;

    @Override
    public AuthorJpa process(@NonNull AuthorMongo author) throws Exception {
        var authorJpa = mapper.toJpa(author);
        authorJpa.setId(utils.generateAuthorJpaTempId(author.getId()));
        return authorJpa;
    }

}
