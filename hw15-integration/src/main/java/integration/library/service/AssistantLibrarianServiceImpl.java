package integration.library.service;

import integration.library.model.Book;
import integration.library.model.DamageLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssistantLibrarianServiceImpl implements AssistantLibrarianService {

    private static final DamageLevel[] DAMAGE_LEVELS = {DamageLevel.LOW, DamageLevel.MEDIUM, DamageLevel.HIGH};

    @Override
    public Book assessDamageLevelBook(Book book) {
        if (book.isDamaged()) {
            book.setDamageLevel(DAMAGE_LEVELS[RandomGenerator.getDefault().nextInt(0, 3)]);
        }
        return book;
    }

    @Override
    public Book putBookOnShelf(Book book) {
        log.info("{}: {}", "Put book on shelf", book.getTitle());
        return book;
    }

}
