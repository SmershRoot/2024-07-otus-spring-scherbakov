package ru.otus.hw.models;

import java.time.LocalDate;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Book book;

    private String text;

    private LocalDate commentDate;


    private String author;

}
