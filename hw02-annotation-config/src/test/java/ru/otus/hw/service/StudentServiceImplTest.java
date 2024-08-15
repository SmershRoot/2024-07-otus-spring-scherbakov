package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void determineCurrentStudent() {
        String firstName = "Smersh";
        String lastName = "Root";

        when(ioService.readStringWithPrompt("Please input your first name")).thenReturn(firstName);
        when(ioService.readStringWithPrompt("Please input your last name")).thenReturn(lastName);

        Student student = studentService.determineCurrentStudent();

        verify(ioService, times(1)).readStringWithPrompt("Please input your first name");
        verify(ioService, times(1)).readStringWithPrompt("Please input your last name");

        assertEquals(firstName, student.firstName(), "First name is not equal " + firstName);
        assertEquals(lastName, student.lastName(), "Last name is not equal " + lastName);
        assertEquals(firstName + " " +lastName, student.getFullName(), "Full name is not correct");
    }

    @Test
    void determineCurrentStudent_withEmptyNames() {
        String firstName = "";
        String lastName = "";

        when(ioService.readStringWithPrompt("Please input your first name")).thenReturn(firstName);
        when(ioService.readStringWithPrompt("Please input your last name")).thenReturn(lastName);

        assertDoesNotThrow(() -> studentService.determineCurrentStudent(), "Exception with empty names");
    }

    @Test
    void determineCurrentStudent_withNullNames() {
        String firstName = null;
        String lastName = null;

        when(ioService.readStringWithPrompt("Please input your first name")).thenReturn(firstName);
        when(ioService.readStringWithPrompt("Please input your last name")).thenReturn(lastName);

        assertDoesNotThrow(() -> studentService.determineCurrentStudent(), "Exception with null names");
    }

}