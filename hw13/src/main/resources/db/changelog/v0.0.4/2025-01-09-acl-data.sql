-- liquibase formatted sql

-- changeset scherbakov_a:2025-01-09--add-tbl-acl-data
-- comment: Добавил данные в системные таблицы под acl
INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 0, 'ROLE_USER'),
(2, 0, 'ROLE_ADMIN'),
(3, 0, 'ROLE_EDITOR'),
(14, 1, 'editor1'),
(15, 1, 'editor2'),
(16, 1, 'user1'),
(17, 1, 'user2');

INSERT INTO acl_class (id, class) VALUES
(101, 'ru.otus.hw.dto.BookDTO'), (102, 'ru.otus.hw.dto.CommentDTO');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1001, 101, 1, NULL, 14, 1),
(1002, 101, 2, NULL, 15, 1),
(1003, 101, 3, NULL, 15, 1);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(2001, 1001, 1, 1, 1, 1, 0, 0),
(2002, 1002, 2, 1, 1, 1, 0, 0),
(2003, 1003, 3, 1, 1, 1, 0, 0),

(2004, 1001, 4, 14, 1, 1, 0, 0),
(2005, 1001, 5, 14, 2, 1, 0, 0),
(2006, 1001, 6, 14, 4, 1, 0, 0),
(2007, 1001, 7, 14, 8, 1, 0, 0),

(2008, 1002, 8, 15, 1, 1, 0, 0),
(2009, 1002, 9, 15, 2, 1, 0, 0),
(2010, 1002, 10, 15, 4, 1, 0, 0),
(2011, 1002, 11, 15, 8, 1, 0, 0),
(2012, 1003, 12, 15, 1, 1, 0, 0),
(2013, 1003, 13, 15, 2, 1, 0, 0),
(2014, 1003, 14, 15, 4, 1, 0, 0),
(2015, 1003, 15, 15, 8, 1, 0, 0)
;