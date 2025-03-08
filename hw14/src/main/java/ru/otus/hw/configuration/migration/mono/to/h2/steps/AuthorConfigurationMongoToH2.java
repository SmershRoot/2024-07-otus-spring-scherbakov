package ru.otus.hw.configuration.migration.mono.to.h2.steps;

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
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.mongo.AuthorMongoRepository;
import ru.otus.hw.utils.MongoToH2Utils;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class AuthorConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final AuthorMongoRepository mongoRepository;

    private final AuthorJpaRepository jpaRepository;

    private final MongoToH2Utils utils;

    @Bean
    public Step migrateAuthorStepMongoToH2(
            RepositoryItemReader<AuthorMongo> authorReaderMongoToH2,
            ItemWriter<AuthorJpa> authorWriterMongoToH2,
            ItemProcessor<AuthorMongo, AuthorJpa> authorProcessorMongoToH2
    ) {
        return new StepBuilder("migrateAuthorStepMongoToH2", jobRepository)
                .<AuthorMongo, AuthorJpa>chunk(3, platformTransactionManager)
                .reader(authorReaderMongoToH2)
                .processor(authorProcessorMongoToH2)
                .writer(authorWriterMongoToH2)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public RepositoryItemReader<AuthorMongo> authorReaderMongoToH2() {
        return new RepositoryItemReaderBuilder<AuthorMongo>()
                .name("authorReaderMongoToH2")
                .repository(mongoRepository)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public AuthorProcessor authorProcessorMongoToH2(AuthorMapper authorMapper) {
        return new AuthorProcessor(authorMapper, utils);
    }

    @Bean
    public ItemWriter<AuthorJpa> authorWriterMongoToH2() {
        return new ItemWriter<AuthorJpa>() {
            @Override
            public void write(@NonNull Chunk<? extends AuthorJpa> chunk) throws Exception {
                chunk.getItems().forEach(author -> {
                    long tempId = author.getId();
                    author.setId(0);
                    jpaRepository.save(author);

                    utils.addAuthorMongoIdAndJpaId(tempId, author.getId());
                });
            }
        };
    }

}
