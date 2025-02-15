-- liquibase formatted sql

--create table authors (
--    id bigserial,
--    full_name varchar(255),
--    primary key (id)
--);

-- changeset scherbakov_a:2025-01-09--add-tbl-acl
-- comment: Добавил системные таблицы под acl
CREATE TABLE IF NOT EXISTS acl_sid (
  id bigint,
  principal smallint NOT NULL,
  sid varchar(100) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_1 UNIQUE(sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id bigint,
  class varchar(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_2 UNIQUE(class)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id bigint auto_increment,
  object_id_class bigint NOT NULL,
  object_id_identity bigint NOT NULL,
  parent_object bigint DEFAULT NULL,
  owner_sid bigint DEFAULT NULL,
  entries_inheriting smallint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_3 UNIQUE (object_id_class,object_id_identity)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id bigint auto_increment,
  acl_object_identity bigint NOT NULL,
  ace_order int NOT NULL,
  sid bigint NOT NULL,
  mask int NOT NULL,
  granting smallint NOT NULL,
  audit_success smallint NOT NULL,
  audit_failure smallint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity,ace_order)
);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);