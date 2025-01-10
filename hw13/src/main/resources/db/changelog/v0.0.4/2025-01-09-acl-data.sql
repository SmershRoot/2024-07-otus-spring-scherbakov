-- liquibase formatted sql

-- changeset scherbakov_a:2025-01-09--add-tbl-acl-data
-- comment: Добавил данные в системные таблицы под acl
INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 0, 'ROLE_USER'),
(2, 0, 'ROLE_ADMIN'),
(3, 0, 'ROLE_EDITOR'),
(14, 1, 'editor1'),
(15, 1, 'editor2');

INSERT INTO acl_class (id, class) VALUES
(101, 'ru.otus.hw.dto.BookDTO'), (102, 'ru.otus.hw.dto.CommentDto');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1001, 101, 1, NULL, 14, 1),
(1002, 101, 2, NULL, 15, 1),
(1003, 101, 3, NULL, 15, 1);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(2001, 1001, 1, 1, 1, 1, 1, 1),
(2002, 1001, 2, 1, 2, 1, 1, 1),
(2003, 1001, 3, 1, 4, 1, 1, 1);