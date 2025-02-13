package ru.otus.hw;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.h2.AuthorJpa;
import ru.otus.hw.models.h2.BookJpa;
import ru.otus.hw.models.h2.CommentJpa;
import ru.otus.hw.models.h2.GenreJpa;
import ru.otus.hw.repositories.h2.AuthorJpaRepository;
import ru.otus.hw.repositories.h2.BookJpaRepository;
import ru.otus.hw.repositories.h2.CommentJpaRepository;
import ru.otus.hw.repositories.h2.GenreJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class MigrationMongoToH2JobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private GenreJpaRepository genreJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void clearMetaData(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void migrateJobMongoToH2Test() throws Exception {
        final Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo("migrateJobMongoToH2");

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        checkAuthors();
        checkGenres();
        checkBooks();
        checkComments();


    }

    private void checkAuthors() {
        var expectedFullNameAuthors = getDbAuthors().stream().map(AuthorJpa::getFullName).toList();
        var actualFullNameAuthors = authorJpaRepository.findAll().stream().map(AuthorJpa::getFullName).toList();
        assertThat(actualFullNameAuthors).containsExactlyInAnyOrderElementsOf(expectedFullNameAuthors);
    }

    private void checkGenres() {
        var expectedNameGenres = getDbGenres().stream().map(GenreJpa::getName).toList();
        var actualNameGenres = genreJpaRepository.findAll().stream().map(GenreJpa::getName).toList();
        assertThat(actualNameGenres).containsExactlyInAnyOrderElementsOf(expectedNameGenres);
    }

    private void checkBooks() {
        var expectedBooks = getDbBooks().stream()
                .map(b -> b.getTitle() + " "
                        + b.getAuthor().getFullName() + " "
                        + b.getGenres().stream().map(GenreJpa::getName)
                                .sorted()
                                .collect(Collectors.joining(","))
                )
                .toList();

        var actualBooks = em.createQuery("select b from BookJpa b", BookJpa.class)
                .setHint("jakarta.persistence.fetchgraph",
                        getGraph())
                .getResultList().stream()
                .map(b -> b.getTitle() + " "
                        + b.getAuthor().getFullName() + " "
                        + b.getGenres().stream().map(GenreJpa::getName)
                            .sorted()
                            .collect(Collectors.joining(",")))
                .toList();
        assertThat(actualBooks).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    public EntityGraph<?> getGraph() {
        var graph = em.createEntityGraph(BookJpa.class);
        graph.addAttributeNodes("author");
        graph.addAttributeNodes("genres");
        return graph;
    }

    private void checkComments() {
        var expectedComments = getDbComments().stream()
                .map(c -> c.getAuthor() + " " + c.getText() + " " +c.getCommentDate() + " "
                        + c.getBook().getTitle())
                .toList();
        var actualComments = commentJpaRepository.findAll().stream()
                .map(c -> c.getAuthor() + " " + c.getText() + " " +c.getCommentDate() + " "
                        + bookJpaRepository.findById(c.getBook().getId())
                        .orElseThrow(() -> new RuntimeException("Book not found"))
                        .getTitle())
                .toList();
        assertThat(actualComments).containsExactlyInAnyOrderElementsOf(expectedComments);
    }




    private static List<AuthorJpa> getDbAuthors() {
        return TestData.getDbAuthors();
    }

    private static List<GenreJpa> getDbGenres() {
        return TestData.getDbGenres();
    }

    private static List<BookJpa> getDbBooks() {
        return TestData.getDbBooks();
    }

    private static List<CommentJpa> getDbComments() {
        return TestData.getDbComments();
    }

}
