package integration.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class BooksFromHuman {

    String fio;

    Set<Book> books;

}
