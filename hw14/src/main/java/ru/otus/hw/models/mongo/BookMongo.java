package ru.otus.hw.models.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"author", "genres"})
@ToString(exclude = {"author", "genres"})
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookMongo {

    @Id
    private String id;

    private String title;

    @DBRef(lazy = true)
    private AuthorMongo author;

    @DBRef(lazy = true)
    private List<GenreMongo> genres;

}
