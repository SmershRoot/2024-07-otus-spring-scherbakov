package integration.library.configuration;

import integration.library.model.Book;
import integration.library.service.AssistantLibrarianServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;

import java.util.List;

@Configuration
public class IntegrationConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100)
                .maxMessagesPerPoll(2);
    }

    @Bean
    public QueueChannelSpec receivedBooksChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> resultChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow libraryBookFlow(
            AssistantLibrarianServiceImpl assistantLibrarianService
    ) {
        return IntegrationFlow
                .from(receivedBooksChannel())
                .split()
                .<Book>log(LoggingHandler.Level.INFO, "Book", bookMessage -> bookMessage.getPayload().getTitle())
                .handle(assistantLibrarianService, "checkBooksForDamage")
                .aggregate()
                .<List<Book>, String>transform(bookMessage -> "" + bookMessage.size())
                .channel(resultChannel())
                .get();
    }

}