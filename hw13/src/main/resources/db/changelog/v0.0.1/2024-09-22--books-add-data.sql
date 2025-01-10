-- liquibase formatted sql

-- changeset scherbakov_a:2024-09-22--books-add-data
-- comment: Добавил данные в таблицу books
insert into books(id, title, author_id)
values (1, 'BookTitle_1', 1), (2, 'BookTitle_2', 2), (3, 'BookTitle_3', 3);