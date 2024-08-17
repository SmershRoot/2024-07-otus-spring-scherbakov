package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.AnswerFormatException;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEXT_FOR_ANSWER = "Please input your answer"
            + " (answer numbers separated by space or comma)";

    private static final String TEXT_ERROR_INPUT_ANSWER = "Please input correct answer";

    private static final String TEXT_ERROR_INPUT_ANSWER_NUMBER = TEXT_ERROR_INPUT_ANSWER
            + " (answer numbers separated by space or comma)";

    private static final String TEXT_ERROR_INPUT_ANSWER_INDEX = TEXT_ERROR_INPUT_ANSWER
            + " The answer option must be within the answer numbers";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
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
        try {
            printQuestion(numberQuestion, question);
            var studentAnswer = ioService.readStringWithPrompt(TEXT_FOR_ANSWER + ":");
            return checkAnswer(question, studentAnswer);
        } catch (AnswerFormatException e) {
            ioService.printFormattedLine(e.getMessage() + "%n");
            return askQuestion(numberQuestion, question);
        }
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

    private boolean checkAnswer(Question question, String studentAnswer) {
        List<Integer> choices = splitAnswer(studentAnswer);
        var questionAnswers = question.answers();
        for (var choice: choices) {
            if (choice < 1 || choice > questionAnswers.size()) {
                throw new AnswerFormatException(TEXT_ERROR_INPUT_ANSWER_INDEX);
            }
            if (!questionAnswers.get(choice - 1).isCorrect()) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> splitAnswer(String answer) {
        try {
            var options = Stream.of(answer.trim().split("[,\\s]+"))
                    .filter(option -> !option.isEmpty()).map(Integer::parseInt).toList();
            if (options.isEmpty()) {
                throw new AnswerFormatException(TEXT_ERROR_INPUT_ANSWER_NUMBER);
            }
            return options;
        } catch (NumberFormatException e) {
            throw new AnswerFormatException(TEXT_ERROR_INPUT_ANSWER_NUMBER);
        }
    }

}
