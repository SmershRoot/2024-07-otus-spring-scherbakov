package integration.library.service;

import integration.library.model.Book;

public interface AssistantLibrarianService {

    Book assessDamageLevelBook(Book book);

    Book putBookOnShelf(Book book);
}
