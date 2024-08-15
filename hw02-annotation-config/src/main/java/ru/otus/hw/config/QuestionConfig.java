package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.constants.Source;

@Configuration
@PropertySource(Source.QUESTION_PATH)
public class QuestionConfig {
}
