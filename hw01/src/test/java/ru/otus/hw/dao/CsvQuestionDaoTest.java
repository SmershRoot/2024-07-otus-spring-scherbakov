package ru.otus.hw.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Тест для Дао-класса {@link CsvQuestionDao}
 * <p>Не юнит-тест (т.к. задействовано файловое хранилище)</p>
 */
@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    private static final String TEST_CORRECT_FILE_NAME = "/test-correct-questions.csv";
    private static final String TEST_NOT_FOUND_FILE_NAME = "/test-not-found-file.csv";
    private static final String TEST_INCORRECT_FILE_NAME = "/test-incorrect-questions.csv";
    private static final String TEST_QUESTION = "Who are the main hosts of the investment show 'Money never sleeps'?";
    private static final List<String> TEST_ANSWER_CORRECT = List.of("Vasily Oleynik", "Irina Akhmadullina");
    private static final List<String> TEST_ANSWER_NOT_CORRECT = List.of("Maxim Orlovsky", "Warren Buffett");

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    /**
     * Получение вопросов из файла. Файл используется не из main, т.к. тут мне нужен
     * файл с эталонными и всеми видами вопросов.
     */
    @Test
    @DisplayName("Получение вопросов из файла")
    void findAllCorrect() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_CORRECT_FILE_NAME);

        var questions = csvQuestionDao.findAll();
        assertThat(questions).isNotEmpty();
    }

    @Test
    @DisplayName("Получение вопросов из файла. Файл отсутствует")
    void findAllIncorrectNotFoundFile() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_NOT_FOUND_FILE_NAME);

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll(),
                "Не получена ошибка при отсутствии файла-источника данных");
    }

    @Test
    @DisplayName("Получение вопросов из файла. Данные в файле не корректны")
    void findAllIncorrectDataUbFile() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_INCORRECT_FILE_NAME);

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll(),
                "Не получена ошибка при не корректности данных в файле");
    }

    @Test
    @DisplayName("Получение тестового(одного из эталонных) вопроса и проверка его ответов")
    void checkQuestionAndAnswer(){
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_CORRECT_FILE_NAME);

        var questions = csvQuestionDao.findAll();
        assertThat(questions).isNotEmpty();
        var countTestQuestion= questions.stream().filter(q -> q.text()
                .equals(TEST_QUESTION))
                        .count();
        assertEquals(countTestQuestion, 1, "Ошибка получения тестового вопроса");

        var testQuestion = questions.stream().filter(q -> q.text()
                        .equals(TEST_QUESTION))
                .findFirst().get();
        var correctAnswer = testQuestion.answers().stream().filter(Answer::isCorrect).map(Answer::text).toList();
        var notCorrectAnswer = testQuestion.answers().stream().filter(a -> !a.isCorrect()).map(Answer::text).toList();
        assertTrue(CollectionUtils.isEqualCollection(correctAnswer, TEST_ANSWER_CORRECT),"Ошибка получения правильного ответа");
        assertTrue(CollectionUtils.isEqualCollection(notCorrectAnswer, TEST_ANSWER_NOT_CORRECT),"Ошибка получения неправильного ответа");
    }

}