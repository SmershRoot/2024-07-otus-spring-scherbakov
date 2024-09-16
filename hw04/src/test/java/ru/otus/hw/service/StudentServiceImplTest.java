package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentServiceImpl studentService;

    @Test
    void determineCurrentStudent() {
        String firstName = "Smersh";
        String lastName = "Root";

        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);

        Student student = studentService.determineCurrentStudent();

        verify(ioService, times(1)).readStringWithPromptLocalized("StudentService.input.first.name");
        verify(ioService, times(1)).readStringWithPromptLocalized("StudentService.input.last.name");

        assertEquals(firstName, student.firstName(), "First name is not equal " + firstName);
        assertEquals(lastName, student.lastName(), "Last name is not equal " + lastName);
        assertEquals(firstName + " " +lastName, student.getFullName(), "Full name is not correct");
    }

    @Test
    void determineCurrentStudent_withEmptyNames() {
        String firstName = "";
        String lastName = "";

        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);

        assertDoesNotThrow(() -> studentService.determineCurrentStudent(), "Exception with empty names");
    }

    @Test
    void determineCurrentStudent_withNullNames() {
        String firstName = null;
        String lastName = null;

        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);

        assertDoesNotThrow(() -> studentService.determineCurrentStudent(), "Exception with null names");
    }

}