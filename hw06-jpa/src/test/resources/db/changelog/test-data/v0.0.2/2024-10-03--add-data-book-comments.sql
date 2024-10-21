-- liquibase formatted sql

-- changeset scherbakov_a:2024-10-03--add-data-book-comments
-- comment: Добавил данные в таблицу comments
insert into comments(book_id, text, comment_date, author)
values (1,'Comment_1_1', '2024-01-01', 'Author_1'),
        (2,'Comment_2', '2024-01-02', 'Author_2'),
        (3,'Comment_3', '2024-01-03', 'Author_3'),
        (1,'Comment_1_2', '2024-01-04', 'Author_1'),
        (1,'Comment_1_3', '2024-01-04', 'Author_2');