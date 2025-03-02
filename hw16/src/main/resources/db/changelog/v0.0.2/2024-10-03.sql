-- liquibase formatted sql

-- changeset scherbakov_a:2024-10-03--add-tbl-comments
-- comment: Добавил таблицу комментариев к книге
create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    text varchar(255),
    comment_date date,
    author varchar(255),
    primary key (id)
);