-- Roles
-- 1
INSERT INTO roles (id, role, created_at, updated_at, deleted_at)
VALUES (1, 'user', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 2
-- INSERT INTO roles (id, role, created_at, updated_at, deleted_at)
-- VALUES (3, 'admin', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 3
-- INSERT INTO roles (id, role, created_at, updated_at, deleted_at)
-- VALUES (2, 'child', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 4
-- INSERT INTO roles (id, role, created_at, updated_at, deleted_at)
-- VALUES (4, 'parent', now(), now(), '1970-01-01 00:00:00+00');
--
--
-- Users
-- 1
INSERT INTO users (id, username, password, created_at, updated_at, deleted_at)
VALUES (1, 'user', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 2
-- INSERT INTO users (id, username, password, created_at, updated_at, deleted_at)
-- VALUES (2, 'admin', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 3
-- INSERT INTO users (id, username, password, created_at, updated_at, deleted_at)
-- VALUES (3, 'child', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');
--
-- -- 4
-- INSERT INTO users (id, username, password, created_at, updated_at, deleted_at)
-- VALUES (4, 'parent', '$2a$10$3', now(), now(), '1970-01-01 00:00:00+00');
--
--

-- Piggy

--1
INSERT INTO piggies (id, name, description, created_at, updated_at, deleted_at)
VALUES (1, 'Piggy 1', 'Piggy 1 description', now(), now(),
        '1970-01-01 00:00:00+00');
