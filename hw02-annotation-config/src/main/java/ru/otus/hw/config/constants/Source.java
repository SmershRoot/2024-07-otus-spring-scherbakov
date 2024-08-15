package ru.otus.hw.config.constants;

import org.springframework.util.ResourceUtils;

/**
 * Константы источников конфигурации
 */
public class Source {

    public static final String ROOT = ResourceUtils.CLASSPATH_URL_PREFIX;

    public static final String EXTENSION = ".properties";

    public static final String APPLICATION = "application";

    public static final String APPLICATION_FILENAME = APPLICATION;

    public static final String APPLICATION_PATH = ROOT + APPLICATION_FILENAME + EXTENSION;

    public static final String QUESTION_FILENAME = "question";

    public static final String QUESTION_PATH = ROOT + QUESTION_FILENAME + EXTENSION;

}
