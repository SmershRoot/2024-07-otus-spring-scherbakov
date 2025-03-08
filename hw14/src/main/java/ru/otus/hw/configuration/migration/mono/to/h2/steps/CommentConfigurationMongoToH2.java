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
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.h2.CommentJpa;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.processors.CommentProcessor;
import ru.otus.hw.repositories.h2.CommentJpaRepository;
import ru.otus.hw.repositories.mongo.CommentMongoRepository;
import ru.otus.hw.utils.MongoToH2Utils;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class CommentConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final CommentMongoRepository mongoRepository;

    private final CommentJpaRepository jpaRepository;

    private final MongoToH2Utils utils;

    @Bean
    public Step migrateCommentStepMongoToH2(
            RepositoryItemReader<CommentMongo> commentReaderMongoToH2,
            ItemWriter<CommentJpa> commentWriterMongoToH2,
            ItemProcessor<CommentMongo, CommentJpa> commentProcessorMongoToH2
    ) {
        return new StepBuilder("migrateCommentStepMongoToH2", jobRepository)
                .<CommentMongo, CommentJpa>chunk(3, platformTransactionManager)
                .reader(commentReaderMongoToH2)
                .processor(commentProcessorMongoToH2)
                .writer(commentWriterMongoToH2)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public RepositoryItemReader<CommentMongo> commentReaderMongoToH2() {
        return new RepositoryItemReaderBuilder<CommentMongo>()
                .name("commentReaderMongoToH2")
                .repository(mongoRepository)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public CommentProcessor commentProcessorMongoToH2(CommentMapper mapper) {
        return new CommentProcessor(mapper, utils);
    }

    @Bean
    public ItemWriter<CommentJpa> commentWriterMongoToH2() {
        return new ItemWriter<CommentJpa>() {
            @Override
            public void write(@NonNull Chunk<? extends CommentJpa> chunk) throws Exception {
                chunk.getItems().forEach(jpaRepository::save);
            }
        };
    }

}
