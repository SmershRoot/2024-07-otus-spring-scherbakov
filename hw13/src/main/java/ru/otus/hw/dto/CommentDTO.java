package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private String text;

    private LocalDate commentDate;

    private String author;

}
