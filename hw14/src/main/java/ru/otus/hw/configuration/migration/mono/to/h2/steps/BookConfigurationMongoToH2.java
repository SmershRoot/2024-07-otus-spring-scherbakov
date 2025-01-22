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
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.repositories.h2.BookJpaRepository;
import ru.otus.hw.repositories.mongo.BookMongoRepository;
import ru.otus.hw.utils.MongoToH2Utils;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class BookConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final BookMongoRepository mongoRepository;

    private final BookJpaRepository jpaRepository;

    private final MongoToH2Utils utils;

    @Bean
    public Step migrateBookStepMongoToH2(
            RepositoryItemReader<BookMongo> bookReaderMongoToH2,
            ItemWriter<BookJpa> bookWriterMongoToH2,
            ItemProcessor<BookMongo, BookJpa> bookProcessorMongoToH2
    ) {
        return new StepBuilder("migrateBookStepMongoToH2", jobRepository)
                .<BookMongo, BookJpa>chunk(3, platformTransactionManager)
                .reader(bookReaderMongoToH2)
                .processor(bookProcessorMongoToH2)
                .writer(bookWriterMongoToH2)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public RepositoryItemReader<BookMongo> bookReaderMongoToH2() {
        return new RepositoryItemReaderBuilder<BookMongo>()
                .name("bookReaderMongoToH2")
                .repository(mongoRepository)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public BookProcessor bookProcessorMongoToH2(BookMapper mapper) {
        return new BookProcessor(mapper, utils);
    }

    @Bean
    public ItemWriter<BookJpa> bookWriterMongoToH2() {
        return new ItemWriter<BookJpa>() {
            @Override
            public void write(@NonNull Chunk<? extends BookJpa> chunk) throws Exception {
                chunk.getItems().forEach(book -> {
                    long tempId = book.getId();
                    book.setId(0);
                    jpaRepository.save(book);

                    utils.addBookMongoIdAndJpaId(tempId, book.getId());
                });
            }
        };
    }

}
