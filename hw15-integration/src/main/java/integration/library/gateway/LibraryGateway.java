package integration.library.gateway;

import integration.library.model.Book;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.Set;

@MessagingGateway
public interface LibraryGateway {

    @Gateway(requestChannel = "receivedBooksChannel", replyChannel = "resultChannel")
    String processLibrarian(Set<Book> books);

}
