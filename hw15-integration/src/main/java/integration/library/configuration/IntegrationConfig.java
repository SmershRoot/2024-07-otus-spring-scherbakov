package integration.library.configuration;

import integration.library.model.Book;
import integration.library.service.AssistantLibrarianService;
import integration.library.service.RepairmanBookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.QueueChannelSpec;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;

import java.util.List;
import java.util.Locale;

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
    public MessageChannelSpec<?, ?> seriousDamageChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public IntegrationFlow seriousDamageFlow(
            AssistantLibrarianService assistantLibrarianService
    ) {
        return IntegrationFlow
                .from(seriousDamageChannel())
                .<Double, String>transform(res -> "Fine: " + String.format(Locale.US, "%.2f",res) + " with warning")
                .get();
    }

    @Bean
    public IntegrationFlow libraryBookFlow(
            AssistantLibrarianService assistantLibrarianService,
            RepairmanBookService repairmanBookService
    ) {
        return IntegrationFlow
                .from(receivedBooksChannel())
                .split()
                .<Book>log(LoggingHandler.Level.INFO, "Book",
                        bookMessage -> "Librarian took the book " + bookMessage.getPayload().getTitle())
                .<Book, Boolean>route(Book::isDamaged, book -> book
                        .subFlowMapping(false, sf -> sf
                                .handle(assistantLibrarianService, "putBookOnShelf")
                        )
                        .subFlowMapping(true, sf -> sf.handle((p, h) -> p)
                        )
                )
                .handle(assistantLibrarianService, "assessDamageLevelBook")
                .<Book, Double>transform(repairmanBookService, "estimateBookRepair")
                .aggregate()
                .<List<Double>, Double>transform(fines -> fines.stream().mapToDouble(Double::doubleValue).sum())
                .<Double, Boolean>route(res -> res > 100,
                        res -> res.subFlowMapping(false, sf -> sf.handle((p, h) -> "Fine: " + String.format(Locale.US, "%.2f", p)))
                                .subFlowMapping(true, sf -> sf.channel(seriousDamageChannel()))
                )
                .channel(resultChannel())
                .get();
    }

}