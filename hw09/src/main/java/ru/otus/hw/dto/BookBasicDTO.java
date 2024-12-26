package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BookBasicDTO {

    private long id;

    private String title;

    @NotNull(message = "Author is required")
    private Long authorId;

    @Size(min = 1, message = "Genres is required")
    @NotEmpty(message = "Genres is required")
    private List<Long> genreIds = new ArrayList<>();
}
