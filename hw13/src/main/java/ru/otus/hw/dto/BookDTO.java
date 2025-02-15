package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private AuthorDTO author;

    private List<GenreDTO> genres;
}
