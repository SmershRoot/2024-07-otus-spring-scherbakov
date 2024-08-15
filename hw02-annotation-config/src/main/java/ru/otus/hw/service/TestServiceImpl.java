package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.domain.TypeAnswerQuestion;
import ru.otus.hw.exceptions.AnswerFormatException;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEXT_FOR_ANSWER = "Please input your answer";

    private static final String TEXT_FOR_ANSWER_WITH_NUMBER = TEXT_FOR_ANSWER
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
            var isAnswerValid = executeQuestion(i + 1, question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    //TODO Проверку данных полученных из csv опустил

    /**
     * Обработка вопроса
     *
     * @param numberQuestion - порядковый номер вопроса
     * @param question - вопрос
     * @return - результат проверки
     */
    private boolean executeQuestion(int numberQuestion, Question question) {
        boolean isAnswerValid;
        try {
            var typeAnswerQuestion = getTypeAnswerQuestion(question);
            var studentAnswer = ascQuestion(typeAnswerQuestion, numberQuestion, question);
            isAnswerValid = checkAnswer(typeAnswerQuestion, question, studentAnswer);
        } catch (AnswerFormatException e) {
            ioService.printFormattedLine(e.getMessage() + "%n");
            isAnswerValid = executeQuestion(numberQuestion, question);
        }
        return isAnswerValid;
    }

    /**
     * Получить тип ожидаемого ответа на вопрос:
     * - Строка - если количество вариантов равно 1
     * - Массив - если количество вариантов более 1
     *
     * @param question - вопрос
     * @return - тип ожидаемого ответа на вопрос
     */
    private TypeAnswerQuestion getTypeAnswerQuestion(Question question) {
        var answers = question.answers();
        return answers.size() == 1 ? TypeAnswerQuestion.STRING_CHOICE : TypeAnswerQuestion.MULTIPLE_CHOICE;
    }

    /**
     * Задать вопрос
     *
     * @param question - вопрос
     */
    private String ascQuestion(TypeAnswerQuestion typeAnswerQuestion, int numberQuestion, Question question) {
        ioService.printFormattedLine("%d. %s", numberQuestion, question.text());
        var answers = question.answers();
        printAnswers(answers);
        var textForAnswer = typeAnswerQuestion.equals(TypeAnswerQuestion.MULTIPLE_CHOICE)
                ? TEXT_FOR_ANSWER_WITH_NUMBER : TEXT_FOR_ANSWER;

        return ioService.readStringWithPrompt(textForAnswer + ":");
    }

    /**
     * Отобразить варианты ответов
     *
     * @param answers - варианты ответов
     */
    private void printAnswers(List<Answer> answers) {
        for (var i = 0; i < answers.size(); i++) {
            var answer = answers.get(i);
            ioService.printFormattedLine("- %d. %s", i + 1, answer.text());
        }
    }

    /**
     * Проверить ответ пользователя
     *
     * @param typeAnswerQuestion - тип ответа на вопрос
     * @param question  - вопрос
     * @param studentAnswer - ответ пользователя/студента
     * @return - результат проверки
     */
    private boolean checkAnswer(TypeAnswerQuestion typeAnswerQuestion, Question question, String studentAnswer) {
        return switch (typeAnswerQuestion) {
            case STRING_CHOICE -> checkStringAnswer(question, studentAnswer);
            case MULTIPLE_CHOICE -> checkMultipleAnswer(question, studentAnswer);
        };
    }

    /**
     * Проверить ответ пользователя
     * <p>Тип ответа - строка</p>
     *
     * @param question - вопрос
     * @param studentAnswer - ответ пользователя/студента
     * @return - результат проверки
     */
    private boolean checkStringAnswer(Question question, String studentAnswer) {
        studentAnswer = studentAnswer.trim().replaceAll("\\s+", " ");
        var questionAnswer = question.answers().get(0);
        return questionAnswer.text().equals(studentAnswer);
    }

    /**
     * Проверить ответ пользователя
     * <p>Тип ответа - строка с перечнем вариантов ответов</p>
     *
     * @param question - вопрос
     * @param studentAnswer - ответ пользователя/студента
     * @return - результат проверки
     */
    private boolean checkMultipleAnswer(Question question, String studentAnswer) {
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

    /**
     * Получение массива ответов
     * <p
     * Подразумеваю что тут будет числовые значения, если не числовые, то вероятно префиксы ответов
     * должны будут содержаться или в источнике или переработать под Map<String,Answer>, где key -
     * это префикс вопроса
     *
     * @param answer - ответ в виде строки введенной пользователем
     * @return массив ответов
     */
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
