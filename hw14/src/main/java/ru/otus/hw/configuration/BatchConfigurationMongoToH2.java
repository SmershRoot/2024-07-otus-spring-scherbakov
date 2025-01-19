package ru.otus.hw.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.mongo.AuthorMongo;

import org.springframework.batch.core.Job;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.repositories.mongo.AuthorMongoRepository;

import java.util.HashMap;

@Configuration
public class BatchConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final AuthorMongoRepository mongoAuthorRepository;

    public BatchConfigurationMongoToH2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, AuthorMongoRepository mongoAuthorRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.mongoAuthorRepository = mongoAuthorRepository;
    }

    @Bean
    public Job migrateJobMongoToH2(Step migrateAuthorStepMongoToH2) {
        return new JobBuilder("migrateJobMongoToH2", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorStepMongoToH2)
                .build();
    }

    @Bean
    public Step migrateAuthorStepMongoToH2(
            RepositoryItemReader<AuthorMongo> authorReaderMongoToH2,
            ItemWriter<AuthorMongo> authorWriterMongoToH2,
            ItemProcessor<AuthorMongo, AuthorMongo> authorProcessorMongoToH2
    ) {
        return new StepBuilder("migrateAuthorStepMongoToH2", jobRepository)
                .<AuthorMongo, AuthorMongo>chunk(3, platformTransactionManager)
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
                .repository(mongoAuthorRepository)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public AuthorProcessor authorProcessorMongoToH2() {
        return new AuthorProcessor();
    }

    @Bean
    public ItemWriter<AuthorMongo> authorWriterMongoToH2() {
        return new ItemWriter<AuthorMongo>() {
            @Override
            public void write(Chunk<? extends AuthorMongo> chunk) throws Exception {
                System.out.println("authorWriterMongoToH2: " + chunk.size());
            }
        };
    }

}
