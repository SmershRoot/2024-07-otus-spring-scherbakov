package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.springframework.batch.core.Job;

import java.time.LocalDateTime;
import java.util.Properties;

import static ru.otus.hw.configuration.migration.mono.to.h2.BatchConfigurationMongoToH2.IMPORT_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchMigrationCommands {

    private final Job migrateJobMongoToH2;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @ShellMethod(value = "startMigrationJobWithJobLauncherMongoToH2", key = "sm-m-2h")
    public void startMigrationJobWithJobLauncherMongoToH2() throws Exception {
        JobExecution execution = jobLauncher.run(migrateJobMongoToH2, new JobParameters());
        System.out.println(execution);
    }

    @ShellMethod(value = "restartMigrationJobWithJobLauncherMongoToH2", key = "sm-m-2h-restart")
    public void restartMigrationJobWithJobLauncherMongoToH2() throws Exception {
        Properties properties = new Properties();
        properties.put("dataTimeRestart", LocalDateTime.now().toString());
        Long executionId = jobOperator.start(IMPORT_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_JOB_NAME));
    }

}
