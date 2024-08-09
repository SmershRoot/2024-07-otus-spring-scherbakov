package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс сгенерировал AI-ассистент разработчика в GigaCode
 * Из интереса и не сказать что очень удачный. В идеале тут бы
 *
 *<p>Класс для тестирования сервиса TestServiceImpl.</p>
 */
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    /**
     * Тест метода executeTest с вопросами.
     */
    @Test
    public void executeTestWithQuestions() {
        List<String> testQuestions = List.of("Question 1", "Question 2");
        List<String> testAnswers = List.of("Answer 1", "Answer 2", "Answer 3", "Answer 4");

        when(questionDao.findAll()).thenReturn(List.of(
                new Question(testQuestions.get(0), List.of(new Answer(testAnswers.get(0), true), new Answer(testAnswers.get(1), false))),
                new Question(testQuestions.get(1), List.of(new Answer(testAnswers.get(2), false), new Answer(testAnswers.get(3), true)))
        ));

        testService.executeTest();
        for(String testQuestion : testQuestions){
            verify(ioService, times(1)).printFormattedLine(anyString() ,any(), eq(testQuestion));
        }
        for(String testAnswer : testAnswers){
            verify(ioService, times(1)).printFormattedLine(anyString() ,any(), eq(testAnswer));
        }
    }

}