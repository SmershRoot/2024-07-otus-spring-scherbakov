-- liquibase formatted sql

-- changeset scherbakov_a:2024-09-22--author-add-data
-- comment: Добавил данные в таблицу authors
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');