package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.springframework.batch.core.Job;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.h2.GenreJpaRepository;

@RequiredArgsConstructor
@ShellComponent
public class BatchMigrationCommands {

    private final Job migrateJobMongoToH2;

    private final Job migrateJobH2ToMongo;

    private final JobLauncher jobLauncher;

    private final AuthorJpaRepository hh;
    private final GenreJpaRepository gg;


    @ShellMethod(value = "startMigrationJobWithJobLauncherMongoToH2", key = "sm-m-2h")
    public void startMigrationJobWithJobLauncherMongoToH2() throws Exception {
        JobExecution execution = jobLauncher.run(migrateJobMongoToH2, new JobParameters());
        System.out.println("TEST HH:"+hh.findAll().size());
        System.out.println("TEST GG:"+gg.findAll().size());
    }

//    @ShellMethod(value = "startMigrationJobWithJobLauncherH2ToMongo", key = "sm-2h-m")
//    public void startMigrationJobWithJobLauncherH2ToMongo() throws Exception {
//        JobExecution execution = jobLauncher.run(migrateJobH2ToMongo, new JobParameters());
//        System.out.println(execution);
//    }

}
