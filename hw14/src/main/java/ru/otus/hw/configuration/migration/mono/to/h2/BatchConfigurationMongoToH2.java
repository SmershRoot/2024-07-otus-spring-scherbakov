package ru.otus.hw.configuration.migration.mono.to.h2;

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
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.mongo.AuthorMongo;

import org.springframework.batch.core.Job;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.repositories.mongo.AuthorMongoRepository;

import java.util.HashMap;

@Configuration
public class BatchConfigurationMongoToH2 {

    private final JobRepository jobRepository;

    public BatchConfigurationMongoToH2(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job migrateJobMongoToH2(
            Step migrateAuthorStepMongoToH2,
            Step migrateGenreStepMongoToH2) {
        return new JobBuilder("migrateJobMongoToH2", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorStepMongoToH2)
                .next(migrateGenreStepMongoToH2)
                .build();
    }

}
