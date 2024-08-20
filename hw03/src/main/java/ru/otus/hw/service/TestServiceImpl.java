package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            var isAnswerValid = askQuestion(i + 1, question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(int numberQuestion, Question question) {
        printQuestion(numberQuestion, question);
        var studentAnswer = ioService.readIntListByForRangeWithPrompt(
                1, question.answers().size(),
                "TestService.answer.input", "TestService.answer.input.error");
        return checkAnswer(question, studentAnswer);
    }

    private void printQuestion(int numberQuestion, Question question) {
        ioService.printFormattedLine("%d. %s", numberQuestion, question.text());
        var answers = question.answers();
        printAnswers(answers);
    }

    private void printAnswers(List<Answer> answers) {
        for (var i = 0; i < answers.size(); i++) {
            var answer = answers.get(i);
            ioService.printFormattedLine("- %d. %s", i + 1, answer.text());
        }
    }

    private boolean checkAnswer(Question question, List<Integer> studentAnswer) {
        var questionAnswers = question.answers();
        for (var choice: studentAnswer) {
            if (!questionAnswers.get(choice - 1).isCorrect()) {
                return false;
            }
        }
        return true;
    }

}
