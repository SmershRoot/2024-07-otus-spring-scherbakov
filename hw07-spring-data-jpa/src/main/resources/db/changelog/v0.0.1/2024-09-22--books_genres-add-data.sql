-- liquibase formatted sql

-- changeset scherbakov_a:2024-09-22--books_genres-add-data
-- comment: Добавил данные в таблицу связи books_genres
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);