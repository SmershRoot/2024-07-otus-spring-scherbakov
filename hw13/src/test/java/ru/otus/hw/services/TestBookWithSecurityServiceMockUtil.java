package ru.otus.hw.services;

import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.TestData;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.mockito.Mockito.when;

public class TestBookWithSecurityServiceMockUtil {

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookWithSecurityService securityService;


    public void addMock() {
        var books = getDbBooks();
        var bookDTOs = getDbBookDTOs();
        when(securityService.readAll()).thenReturn(books);
        when(securityService.findById(bookDTOs.get(0).getId())).thenReturn(books.get(0));
        when(securityService.create((books.get(0)))).thenReturn(books.get(0));
        when(securityService.update(books.get(0))).thenReturn(books.get(0));

        when(bookMapper.toBookDTO(books.get(0))).thenReturn(bookDTOs.get(0));
        when(bookMapper.toBookDTO(books.get(1))).thenReturn(bookDTOs.get(1));
        when(bookMapper.toBookDTO(books.get(2))).thenReturn(bookDTOs.get(2));
        when(bookMapper.toBook(bookDTOs.get(0))).thenReturn(books.get(0));

    }

    private static List<Book> getDbBooks() {
        return TestData.getDbBooks();
    }

    protected static List<BookDTO> getDbBookDTOs() {
        return TestData.getDbBookDTOs();
    }

}
