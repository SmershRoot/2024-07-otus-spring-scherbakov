package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    private static final String TEST_CORRECT_FILE_NAME = "/test-correct-questions.csv";
    private static final String TEST_NOT_FOUND_FILE_NAME = "/test-not-found-file.csv";
    private static final String TEST_INCORRECT_FILE_NAME = "/test-incorrect-questions.csv";

    @Mock
    TestFileNameProvider fileNameProvider;

    @InjectMocks
    CsvQuestionDao csvQuestionDao;

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
    @DisplayName("Ошибка получение вопросов из файла. Файл отсутствует")
    void findAllIncorrectNotFoundFile() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_NOT_FOUND_FILE_NAME);

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll(),
                "Не получена ошибка при отсутствии файла-источника данных");
    }

    @Test
    @DisplayName("Ошибка получение вопросов из файла. Данные в файле не корректны")
    void findAllIncorrectDataUbFile() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_INCORRECT_FILE_NAME);

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll(),
                "Не получена ошибка при не корректности данных в файле");
    }

}