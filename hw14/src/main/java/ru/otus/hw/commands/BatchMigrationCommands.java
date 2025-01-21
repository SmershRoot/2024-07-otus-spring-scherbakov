package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.springframework.batch.core.Job;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.h2.BookJpaRepository;
import ru.otus.hw.repositories.h2.GenreJpaRepository;
import ru.otus.hw.utils.MongoToH2Utils;

@RequiredArgsConstructor
@ShellComponent
public class BatchMigrationCommands {

    private final Job migrateJobMongoToH2;

    private final Job migrateJobH2ToMongo;

    private final JobLauncher jobLauncher;

    private final AuthorJpaRepository hh;

    private final GenreJpaRepository gg;

    private final BookJpaRepository bookR;

    private final MongoToH2Utils mongoToH2Utils;


    @ShellMethod(value = "startMigrationJobWithJobLauncherMongoToH2", key = "sm-m-2h")
    public void startMigrationJobWithJobLauncherMongoToH2() throws Exception {
        JobExecution execution = jobLauncher.run(migrateJobMongoToH2, new JobParameters());



        System.out.println("TEST HH:" + hh.findAll().size());
        hh.findAll().forEach(h -> System.out.println(h.getId() + " " + h.getFullName()));

        System.out.println("TEST GG:" + gg.findAll().size());
        System.out.println("TEST BB:" + bookR.findAll());
    }

    @ShellMethod(value = "startMigrationJobWithJobLauncherMongoToH2", key = "test")
    @Transactional
    public void test() throws Exception {
        bookR.findAll()
                .forEach(b -> System.out.println(b.getId() + " " + b.getTitle() + " " + b.getAuthor().getFullName()));
    }


//    @ShellMethod(value = "startMigrationJobWithJobLauncherH2ToMongo", key = "sm-2h-m")
//    public void startMigrationJobWithJobLauncherH2ToMongo() throws Exception {
//        JobExecution execution = jobLauncher.run(migrateJobH2ToMongo, new JobParameters());
//        System.out.println(execution);
//    }

}
