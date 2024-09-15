package ru.otus.hw.service;

import java.util.List;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    void printFormattedLineLocalized(String code, Object ...args);

    String readStringWithPromptLocalized(String promptCode);

    int readIntForRangeLocalized(int min, int max, String errorMessageCode);

    int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode);

    List<Integer> readIntListByForRangeLocalized(int min, int max, String errorMessage);

    List<Integer> readIntListByForRangeWithPromptLocalized(int min, int max, String prompt, String errorMessage);

}
