package ru.otus.hw.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BookBasicDTO {

    private long id;

    private String title;

    private Long authorId;

    private List<Long> genreIds = new ArrayList<>();
}
