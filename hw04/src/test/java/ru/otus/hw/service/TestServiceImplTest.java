package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestServiceImpl.class})
class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @Test
    void executeTestFor() {
        List<String> testQuestions = List.of("Question 1", "Question 2");
        List<String> testAnswers = List.of("Answer 1", "Answer 2", "Answer 3", "Answer 4");

        when(questionDao.findAll()).thenReturn(List.of(
                new Question(testQuestions.get(0), List.of(new Answer(testAnswers.get(0), true), new Answer(testAnswers.get(1), false))),
                new Question(testQuestions.get(1), List.of(new Answer(testAnswers.get(2), false), new Answer(testAnswers.get(3), true)))
        ));
        when(ioService.readIntListByForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(List.of(1));

        TestResult result = testService.executeTestFor(new Student("Smersh", "Root"));
        assertEquals(1, result.getRightAnswersCount(), "Not correct result for RightAnswersCount");
        assertEquals(2, result.getAnsweredQuestions().size(), "Not correct result for AnsweredQuestions");

        for (Question q : result.getAnsweredQuestions()) {
            assertTrue(testQuestions.contains(q.text()), "Result question is not contains in testQuestions");
        }

    }
}