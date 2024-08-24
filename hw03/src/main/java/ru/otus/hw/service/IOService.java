package ru.otus.hw.service;

import java.util.List;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    String readString();

    String readStringWithPrompt(String prompt);

    int readIntForRange(int min, int max, String errorMessage);

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);

    List<Integer> readIntListByForRange(int min, int max, String errorMessage);

    List<Integer> readIntListByForRangeWithPrompt(int min, int max, String prompt, String errorMessage);

}
