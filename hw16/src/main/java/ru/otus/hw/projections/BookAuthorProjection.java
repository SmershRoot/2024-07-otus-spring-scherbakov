package ru.otus.hw.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.hw.models.Book;

@Projection(name = "virtual", types = { Book.class })
public interface BookAuthorProjection {

    @Value("#{target.author.fullName}")
    String getAuthorName();

    String getTitle();
}