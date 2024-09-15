package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Commands for run testing")
@RequiredArgsConstructor
public class TestRunnerCommands {

    private final LoginContext loginContext;

    private final TestRunnerService testRunnerService;
    
    @ShellMethod(value = "Enter First and Last name {fn, firstname} and {ln, lastname}", key = {"l", "login", "fl-name"})
    public String login(
            @ShellOption(help = "Enter First {fn, firstname} name", value = {"fn, firstname"}) String firstName,
            @ShellOption(help = "Enter Last {ln, lastname} name", value = {"ln, lastname"}) String lastName
    ) {
        loginContext.login(firstName, lastName);
        return "LOGGED IN";
    }

    @ShellMethod(value = "Run test", key = {"run"})
    @ShellMethodAvailability("availableRunTest")
    public void runTest() {
        testRunnerService.run();
    }

    private Availability availableRunTest() {
        if (loginContext.isAuthenticated()) {
            return Availability.available();
        }
        return Availability.unavailable("Pleas login. Use 'l' or 'login' command to login");
    }

}
