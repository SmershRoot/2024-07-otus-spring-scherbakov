package integration.library.commands;

import integration.library.service.LibrarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class IntegrationCommands {

    private final LibrarianService librarianService;

    @ShellMethod(value = "startIntegration", key = "start")
    public void startIntegration() {
        librarianService.startProcessReturnBooks();
    }

}
