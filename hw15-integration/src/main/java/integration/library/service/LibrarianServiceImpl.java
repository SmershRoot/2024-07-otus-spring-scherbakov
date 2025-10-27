package integration.library.service;

import integration.library.gateway.LibraryGateway;
import integration.library.model.Book;
import integration.library.model.BooksFromHuman;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibrarianServiceImpl implements LibrarianService {

    private final LibraryGateway libraryGateway;

    @Override
    public void startProcessReturnBooks() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < RandomGenerator.getDefault().nextInt(5, 10); i++) {
            int finalI = i;
            pool.execute(() -> {
                var booksFromHuman = generateBooksFromHuman(finalI);
                log.info("{}, Librarian get books from human: {}: {}", finalI + 1,
                        booksFromHuman.getFio(),
                        booksFromHuman.getBooks().stream().map(Book::getTitle).collect(Collectors.joining(","))
                );
                var books = booksFromHuman.getBooks();
                String result = libraryGateway.processLibrarian(books);
                log.info("{}, RESULT {}", booksFromHuman.getFio(), result);
            });
        }
    }

    private BooksFromHuman generateBooksFromHuman(int i) {
        return new BooksFromHuman("Human" + i, generateBooks(i));
    }

    private Set<Book> generateBooks(int iHuman) {
        var books = new HashSet<Book>();
        for (int i = 0; i < RandomGenerator.getDefault().nextInt(1, 5); i++) {
            boolean isDamaged = RandomGenerator.getDefault().nextBoolean();
            double worth = RandomGenerator.getDefault().nextDouble(0,1);

            Book book = new Book("Book" + +iHuman + "_" + i, null, worth, isDamaged);
            books.add(book);
        }

        return books;
    }




}
