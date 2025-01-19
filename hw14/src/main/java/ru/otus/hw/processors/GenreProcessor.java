package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.models.h2.GenreJpa;
import ru.otus.hw.models.mongo.GenreMongo;

@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<GenreMongo, GenreJpa> {

    private final GenreMapper mapper;

    @Override
    public GenreJpa process(@NonNull GenreMongo genre) throws Exception {
        return mapper.toGenreJpa(genre);
    }
}
