package ru.otus.hw.configuration.migration.mono.to.h2;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.h2.GenreJpa;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.repositories.h2.GenreJpaRepository;
import ru.otus.hw.repositories.mongo.GenreMongoRepository;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class GenreConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final GenreMongoRepository mongoRepository;

    private final GenreJpaRepository jpaRepository;

    @Bean
    public Step migrateGenreStepMongoToH2(
            RepositoryItemReader<GenreMongo> genreReaderMongoToH2,
            ItemWriter<GenreJpa> genreWriterMongoToH2,
            ItemProcessor<GenreMongo, GenreJpa> genreProcessorMongoToH2
    ) {
        return new StepBuilder("migrateAuthorStepMongoToH2", jobRepository)
                .<GenreMongo, GenreJpa>chunk(3, platformTransactionManager)
                .reader(genreReaderMongoToH2)
                .processor(genreProcessorMongoToH2)
                .writer(genreWriterMongoToH2)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public RepositoryItemReader<GenreMongo> genreReaderMongoToH2() {
        return new RepositoryItemReaderBuilder<GenreMongo>()
                .name("genreReaderMongoToH2")
                .repository(mongoRepository)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public GenreProcessor genreProcessorMongoToH2(GenreMapper genreMapper) {
        return new GenreProcessor(genreMapper);
    }

    @Bean
    public ItemWriter<GenreJpa> genreWriterMongoToH2() {
        return new ItemWriter<GenreJpa>() {
            @Override
            public void write(@NonNull Chunk<? extends GenreJpa> chunk) throws Exception {
                chunk.getItems().forEach(genre -> {
                    //ТУТ Изменение мапы с идентификаторами
                    jpaRepository.save(genre);
                });
            }
        };
    }

}
