package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (var inputStream = getClass().getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (Objects.isNull(inputStream)) {
                throw new QuestionReadException("The source of data is not available");
            }
            Reader reader = new InputStreamReader(inputStream);
            var questionDtos = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
            return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
        } catch (QuestionReadException e) {
            throw e;
        } catch (IOException e) {
            throw new QuestionReadException("Error interacting with data source", e);
        } catch (Exception e) {
            throw new QuestionReadException("Error reading data", e);
        }
    }
}
