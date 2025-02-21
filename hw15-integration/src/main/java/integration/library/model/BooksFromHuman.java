package integration.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class BooksFromHuman {

    private String fio;

    private Set<Book> books;

}
