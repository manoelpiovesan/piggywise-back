-- Family

--1
INSERT INTO families (name, code, description, created_at, updated_at, deleted_at)
VALUES ('Rodrigues', 'XX00YY', 'Family 1 description', now(), now(), '1970-01-01 00:00:00+00');

-- Roles
-- 1
INSERT INTO roles (name, created_at, updated_at, deleted_at)
VALUES ('user', now(), now(), '1970-01-01 00:00:00+00');

-- 2
INSERT INTO roles (name, created_at, updated_at, deleted_at)
VALUES ('admin', now(), now(), '1970-01-01 00:00:00+00');

-- 3
INSERT INTO roles (name, created_at, updated_at, deleted_at)
VALUES ('child', now(), now(), '1970-01-01 00:00:00+00');

-- 4
INSERT INTO roles (name, created_at, updated_at, deleted_at)
VALUES ('parent', now(), now(), '1970-01-01 00:00:00+00');


-- Users
-- 1
INSERT INTO users (username, family_id, name, password, created_at, updated_at, deleted_at)
VALUES ('user', 1,'Nome do Usuario', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');

-- 2
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('admin', 'Nome do Usuario', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');

-- 3
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('child', 'Nome do Usuario', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');

-- 4
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('parent', 'Nome do Usuario', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');

-- 5
INSERT INTO users (username, family_id, name, password, created_at, updated_at, deleted_at)
VALUES ('manoel2', 1, 'Manoel Rodrigues', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');

-- Users x Roles
-- 1 user x user
INSERT INTO users_roles (user_id, roles_id)
VALUES (1, 1);

-- 2 admin x admin
INSERT INTO users_roles (user_id, roles_id)
VALUES (2, 3);

-- 3 child x child
INSERT INTO users_roles (user_id, roles_id)
VALUES (3, 2);

-- 4 parent x parent
INSERT INTO users_roles (user_id, roles_id)
VALUES (4, 4);

-- 5 manoel2 x child
INSERT INTO users_roles (user_id, roles_id)
VALUES (5, 3);

-- Piggy

--1
INSERT INTO piggies (code, name, description, created_at, updated_at, deleted_at)
VALUES ('PIGGY1', 'Piggy 1', 'Piggy 1 description', now(), now(), '1970-01-01 00:00:00+00');

--1
INSERT INTO piggies (family_id, code, name, description, created_at, updated_at, deleted_at)
VALUES (1,'PIGGY2', 'Piggy 2', 'Piggy 2 description', now(), now(), '1970-01-01 00:00:00+00');



