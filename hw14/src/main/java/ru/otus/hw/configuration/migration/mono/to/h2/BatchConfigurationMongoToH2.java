package ru.otus.hw.configuration.migration.mono.to.h2;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import org.springframework.batch.core.Job;
import ru.otus.hw.utils.MongoToH2Utils;

@Configuration
public class BatchConfigurationMongoToH2 {

    public static final String IMPORT_JOB_NAME = "migrateJobMongoToH2";

    private final JobRepository jobRepository;

    private final MongoToH2Utils mongoToH2Utils;

    public BatchConfigurationMongoToH2(JobRepository jobRepository, MongoToH2Utils mongoToH2Utils) {
        this.jobRepository = jobRepository;
        this.mongoToH2Utils = mongoToH2Utils;
    }

    @Bean
    public Job migrateJobMongoToH2(
            Step migrateAuthorStepMongoToH2,
            Step migrateGenreStepMongoToH2,
            Step migrateBookStepMongoToH2,
            Step migrateCommentStepMongoToH2) {
        return new JobBuilder(IMPORT_JOB_NAME, jobRepository)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        mongoToH2Utils.h2ClearAll();
                    }
                })
                .incrementer(new RunIdIncrementer())
                .start(startSlitFlow(migrateAuthorStepMongoToH2, migrateGenreStepMongoToH2))
                .next(migrateBookStepMongoToH2)
                .next(migrateCommentStepMongoToH2)
                .build()
                .build();
    }

    @Bean
    public Flow startSlitFlow(Step migrateAuthorStepMongoToH2, Step migrateGenreStepMongoToH2) {
        return new FlowBuilder<Flow>("startSlitFlowMongoToH2")
                .split(new SimpleAsyncTaskExecutor("tascStartSolitFlowMingoToH2"))
                .add(
                        new FlowBuilder<SimpleFlow>("flowAuthorStepMongoToH2").start(migrateAuthorStepMongoToH2).build(),
                        new FlowBuilder<SimpleFlow>("flowAGenreStepMongoToH2").start(migrateGenreStepMongoToH2).build()
                )
                .build();
    }

}
