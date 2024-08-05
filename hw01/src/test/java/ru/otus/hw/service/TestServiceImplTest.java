package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс сгенерировал AI-ассистент разработчика в GigaCode
 * Из интереса и не сказать что очень удачный. В идеале тут бы
 *
 *<p>Класс для тестирования сервиса TestServiceImpl.</p>
 */
public class TestServiceImplTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private PrintStream printStream;

    /**
     * Мок для класса IOService.
     */
    private IOService ioService;

    /**
     * Мок для класса QuestionDao.
     */
    private QuestionDao questionDao;

    /**
     * Экземпляр класса TestServiceImpl.
     */
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        printStream = new PrintStream(outputStreamCaptor);
        ioService = new StreamsIOService(printStream);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    /**
     * Тест метода executeTest с вопросами.
     */
    @Test
    public void executeTest_withQuestions() {
        List<String> testQuestions = List.of("Question 1", "Question 2");
        List<String> testAnswers = List.of("Answer 1", "Answer 2", "Answer 3", "Answer 4");

        when(questionDao.findAll()).thenReturn(List.of(
                new Question(testQuestions.get(0), List.of(new Answer(testAnswers.get(0), true), new Answer(testAnswers.get(1), false))),
                new Question(testQuestions.get(1), List.of(new Answer(testAnswers.get(2), false), new Answer(testAnswers.get(3), true)))
        ));

        testService.executeTest();

        for(String testQuestion : testQuestions){
            assertTrue(outputStreamCaptor.toString().contains(testQuestion),"Вывод данных не содержит вопроса: " + testQuestion);
        }
        for(String testAnswer : testAnswers){
            assertTrue(outputStreamCaptor.toString().contains(testAnswer),"Вывод данных не содержит варианта ответа: " + testAnswer);
        }
    }

}