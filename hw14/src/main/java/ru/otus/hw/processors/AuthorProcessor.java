package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.lang.NonNull;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.mongo.AuthorMongo;

@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<AuthorMongo, AuthorJpa> {

    private final AuthorMapper mapper;

    @Override
    public AuthorJpa process(@NonNull AuthorMongo author) throws Exception {
        return mapper.toAuthorJpa(author);
    }

}
