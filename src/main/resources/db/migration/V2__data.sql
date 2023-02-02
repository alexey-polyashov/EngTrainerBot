--TABLES

--BOT_USERS
INSERT INTO bot_users (id, chat_id, email, name, marked, created_at, updated_at)
VALUES (1, 0, 'polyashofff@yandex.ru', 'Aleksey', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
        (2, 0, 'janya@yandex.ru', 'Janin', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');

--ROLES
INSERT INTO roles (id, name, description, marked, created_at, updated_at)
VALUES (1, 'ADMIN', 'Администраторы', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
        (2, 'USER', 'Прочие пользователи', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');

--USER_ROLES
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),(1, 2),
        (2, 2);

--DICTIONARIES
INSERT INTO dictionaries (id, description, name, marked, created_at, updated_at)
VALUES (1, 'My dictionary', 'Provide to create your own dictionary', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');