package integration.library.service;

import integration.library.model.Book;
import integration.library.model.DamageLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepairmanBookServiceImpl implements RepairmanBookService {

    @Override
    public Double estimateBookRepair(Book book) {
        if (!book.isDamaged()) {
            return 0.0;
        }
        if (book.getDamageLevel().equals(DamageLevel.HIGH)) {
            log.info("{} sent for disposal", book.getTitle());
        }

        return switch (book.getDamageLevel()) {
            case LOW -> 100 * book.getWorth();
            case MEDIUM -> 200 * book.getWorth();
            case HIGH -> 300 * book.getWorth();
        };

    }
}
