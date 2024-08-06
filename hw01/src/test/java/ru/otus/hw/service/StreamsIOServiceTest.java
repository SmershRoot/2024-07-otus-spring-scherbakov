package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;

    @InjectMocks
    private StreamsIOService ioService;

    @Test
    @DisplayName(value = "Тест метода ioService.printLine должен выводить сообщение в консоль")
    void printLine() {
        String testString = "Message for test";
        ioService.printLine(testString);
        verify(printStream, times(1)).println(testString);
    }

    /**
     * Тест метода ioService.printFormattedLine должен выводить сообщение в консоль с параметрами с учетом модификаций сообщения
     */
    @DisplayName(value = "Тест метода ioService.printFormattedLine должен выводить сообщение в консоль с параметрами")
    @RepeatedTest(5)
    void printFormattedLine() {
        String testString = generateRandomString(100);
        String stringArg = generateRandomString(10);
        int intArg = new Random().nextInt();
        ioService.printFormattedLine(testString, stringArg, intArg);
        verify(printStream, times(1)).printf(testString + "%n", stringArg, intArg);
    }

    /**
     * Генерация случайной строки заданной длины
     *
     * @param length - длина строки
     * @return - случайная строка
     */
    private String generateRandomString(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array);
    }
}