package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private long id;

    @NotNull
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private AuthorDTO author;

    private List<GenreDTO> genres;
}
