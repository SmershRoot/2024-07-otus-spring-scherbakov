package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

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
     * Тест метода ioService.printFormattedLine должен выводить сообщение в консоль с параметрами
     * <p>Посчитал что нормальное поведение если текст перед вызовом printf может быть в будущем модифицирован,
     * например приведение к верхнему регистру или добавление двоеточия перед переносом.
     * Потому проверяю на anyString(), а не на testString + "%n"</p>
     */
    @Test
    @DisplayName(value = "Тест метода ioService.printFormattedLine должен выводить сообщение в консоль с параметрами")
    void printFormattedLine() {
        String testString = "Message for test";
        String string_arg = "string_arg";
        int int_arg = 1;
        ioService.printFormattedLine(testString, string_arg, int_arg);
        verify(printStream, times(1)).printf(anyString(), eq(string_arg), eq(int_arg));
    }
}