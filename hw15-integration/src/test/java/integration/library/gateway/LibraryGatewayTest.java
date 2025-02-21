package integration.library.gateway;

import integration.library.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@SpringIntegrationTest
public class LibraryGatewayTest {

    @Autowired
    private LibraryGateway libraryGateway;

    @Test
    @DisplayName("All books are not damaged")
    void processLibrarian_allBooksAreNotDamagedTest() {
        var expectedResult = "Fine: 0.0";

        Set<Book> books = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            books.add(new Book("Book_"+i, null, 1, false));
        }

        String actualResult = libraryGateway.processLibrarian(books);
        Assertions.assertThat(actualResult).contains(expectedResult);
    }

    @Test
    @DisplayName("Books with damaged")
    void processLibrarian_BooksWithDamagedTest() {
        Set<Book> books = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            books.add(new Book("Book_"+i, null, 1, false));
        }
        var bookWithDamagedInput = new Book("Book_with_damaged", null, 0.1, true);
        books.add(bookWithDamagedInput);

        String actualResult = libraryGateway.processLibrarian(books);
        Assertions.assertThat(actualResult).contains("Fine:");
        Assertions.assertThat(actualResult).doesNotContain (" 0.0", "with warning");
    }

    @Test
    @DisplayName("Books with serious damaged")
    void processLibrarian_BooksWithSeriousDamagedTest() {
        Set<Book> books = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            books.add(new Book("Book_"+i, null, 1, false));
        }
        books.add(new Book("Book_with_damaged_1", null, 1, true));
        books.add(new Book("Book_with_damaged_2", null, 1, true));

        String actualResult = libraryGateway.processLibrarian(books);
        Assertions.assertThat(actualResult).contains("Fine:", "with warning");
        Assertions.assertThat(actualResult).doesNotContain (" 0.0");
    }


}
