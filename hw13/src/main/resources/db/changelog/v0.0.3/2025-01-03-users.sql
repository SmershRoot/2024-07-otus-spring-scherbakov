-- liquibase formatted sql

-- changeset scherbakov_a:2025-01-03--add-tbl-users-roles
-- comment: Добавил таблицу пользователей и ролей
create table users (
                       id bigserial,
                       username varchar(255),
                       password varchar(255),
                       primary key (id)
);
create table roles (
    id bigserial,
    name varchar(255) not null unique,
    primary key (id)
);
create table users_roles (
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (user_id, role_id)
);

-- changeset scherbakov_a:2025-01-03--add-tbl-users-roles-data
-- comment: Добавил данные пользователей и ролей
insert into users(id, username, password)
values (1, 'user1', '$2a$04$QN6eLV125r1wb26FnG/ZzejoF1xcg1nZ49aWMI6VZ4IzgoGW7E/yy'),
 (2, 'admin', '$2a$04$QN6eLV125r1wb26FnG/ZzejoF1xcg1nZ49aWMI6VZ4IzgoGW7E/yy'),
 (3, 'editor1', '$2a$04$QN6eLV125r1wb26FnG/ZzejoF1xcg1nZ49aWMI6VZ4IzgoGW7E/yy'),
 (4, 'editor2', '$2a$04$QN6eLV125r1wb26FnG/ZzejoF1xcg1nZ49aWMI6VZ4IzgoGW7E/yy'),
 (5, 'user2', '$2a$04$QN6eLV125r1wb26FnG/ZzejoF1xcg1nZ49aWMI6VZ4IzgoGW7E/yy')
 ;

insert into roles(id, name)
values (1, 'USER'), (2, 'ADMIN'), (3, 'EDITOR');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 1), (2, 2), (3, 3), (4, 3), (5, 1);