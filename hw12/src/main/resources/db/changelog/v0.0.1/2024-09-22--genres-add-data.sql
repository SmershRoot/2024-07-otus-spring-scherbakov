-- liquibase formatted sql

-- changeset scherbakov_a:2024-09-22--genres-add-data
-- comment: Добавил данные в таблицу genres
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');