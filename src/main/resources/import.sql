-- Family

--1
INSERT INTO families (name, code, description, created_at, updated_at,
                      deleted_at)
VALUES ('Desenvolvimento de Produtos', 'DEVPROD', 'Family 1 description', now(),
        now(),
        '1970-01-01 00:00:00+00');

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
INSERT INTO users (username, family_id, name, password, created_at, updated_at,
                   deleted_at)
VALUES ('user', 1, 'Nome do Usuario', 'manoel', now(), now(),
        '1970-01-01 00:00:00+00');

-- 2
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('admin', 'Nome do Usuario', 'manoel', now(), now(),
        '1970-01-01 00:00:00+00');

-- 3
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('child', 'Nome do Usuario', 'manoel', now(), now(),
        '1970-01-01 00:00:00+00');

-- 4
INSERT INTO users (username, name, password, created_at, updated_at, deleted_at)
VALUES ('parent', 'Nome do Usuario', 'manoel', now(), now(),
        '1970-01-01 00:00:00+00');

-- 5
INSERT INTO users (username, family_id, name, password, created_at, updated_at,
                   deleted_at)
VALUES ('manoel2', 1, 'Manoel Rodrigues', 'manoel', now(), now(),
        '1970-01-01 00:00:00+00');

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
INSERT INTO piggies (family_id, balance, code, name, description,
                     created_at, updated_at,
                     deleted_at)
VALUES (1, 0, 'PIGGY1', 'Piggy 1', 'Piggy 1 description', now(), now(),
        '1970-01-01 00:00:00+00');

--2
INSERT INTO piggies (balance, code, name, description,
                     created_at, updated_at,
                     deleted_at)
VALUES (0, 'PIGGY2', 'Piggy 2', 'Piggy 2 description', now(), now(),
        '1970-01-01 00:00:00+00');

--3
INSERT INTO piggies (balance, code, name, description,
                     created_at, updated_at,
                     deleted_at)
VALUES (0, 'PIGGY3', 'Piggy 3', 'Piggy 3 description', now(), now(),
        '1970-01-01 00:00:00+00');

-- Task

-- 1
INSERT INTO tasks (piggy_id, name, description, points, status, due_date,
                   created_at,
                   updated_at, deleted_at)
VALUES (1, 'Lixo', 'Colocar o lixo para fora.', 100, 'pending',
        now() + interval '1 day', now(), now(),
        '1970-01-01 00:00:00+00');

-- 2
INSERT INTO tasks (piggy_id, name, description, points, status, due_date,
                   created_at,
                   updated_at, deleted_at)
VALUES (1, 'Lição de Casa', 'Fazer os exercícios da página 2 à 8.', 200, 'waiting_approval',
        now() + interval '1 day', now(), now(),
        '1970-01-01 00:00:00+00');

-- 3
INSERT INTO tasks (piggy_id, name, description, points, status, due_date,
                   created_at,
                   updated_at, deleted_at)
VALUES (1, 'Lavar a Louça', 'Lavar após a janta.', 960, 'done',
        now() + interval '1 day', now(), now(),
        '1970-01-01 00:00:00+00');


-- Rewards

--1
INSERT INTO rewards (piggy_id, name, description, points, created_at,
                     updated_at, deleted_at)
VALUES (1, 'Reward 1', 'Reward 1 description', 1000, now(), now(),
        '1970-01-01 00:00:00+00');

--2
INSERT INTO rewards (piggy_id, name, description, points, created_at,
                     updated_at, deleted_at)
VALUES (1, 'Reward 2', 'Reward 2 description', 2000, now(), now(),
        '1970-01-01 00:00:00+00');