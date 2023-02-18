--TABLES

--BOT_USERS
INSERT INTO bot_users (chat_id, email, name, marked, created_at, updated_at)
VALUES (0, 'polyashofff@yandex.ru', 'Aleksey', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
        (0, 'janya@yandex.ru', 'Janin', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');

--ROLES
INSERT INTO roles (id, name, description, marked, created_at, updated_at)
VALUES (1, 'ADMIN', 'Администраторы', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
        (2, 'USER', 'Прочие пользователи', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');

--USER_ROLES
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),(1, 2),
        (2, 2);

--DICTIONARIES
INSERT INTO dictionaries (name, description, marked, created_at, updated_at)
VALUES ('My dictionary', 'Provide to create your own dictionary', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
 ('Base words 100', 'More popular 100 words', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
 ('Base words 1000', 'More popular 1000 words', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
 ('Base adjectives 100', 'More popular adjectives', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00'),
 ('Man`s body', 'Words about parts of man body', false, '2022-01-01 08:30:00', '2022-01-01 08:30:00');

--WORDS
INSERT INTO words (foreign_write, transcription, native_write, description)
VALUES ('for', '[fɔː]', 'для', 'for example - например, for all - для всего, need for - нужно для, looking for - искать'),
 ('friend', '[frend]', 'друг, приятель, дружить', 'my friend - мой друг, close friend - близкий друг, best friend - лучший друг, to be you friend -дружить с тобой'),
 ('it', '[ɪt]', 'это, он, оно', 'it`s a picture - это картинка, it actually happened  - это призошло'),
 ('month', '[mʌnθ]', 'месяц', 'at this month - в этом месяце, month after month - месяц за месяцем'),
 ('that', '[ðæt]', 'что, который, это, чтобы', 'that means - это означает, that happens - это происходит, i believe that we can - я верю что мы можем'),
 ('in', '[ɪn]', 'в', 'in the house - в доме, in here - здесь, in order - в порядке'),
 ('and', '[ænd]', 'и', 'you and i - ты и я, and so on - и так далее, pros and cons - плюсы и минусы'),
 ('to', '[tuː]', 'к, в, на, до', 'to the left - налево, to count to ten - считать до десяти, close to - вблизи от, go to slip - заснуть'),
 ('have', '[hæv]', 'иметь, быть, есть', 'we have - у нас есть, people who have - люди которые имеют, i might have - у меня може быть, everyone should have - каждый должен иметь, i have a body - у меня есть тело');

--WORDS_DICTIONARIES
INSERT INTO words_dictionaries (words_id, dictionaries_id)
VALUES (1, 2),
 (2, 2),
 (3, 2),
 (4, 2),
 (5, 2),
 (6, 2),
 (7, 2),
 (8, 2),
 (9, 2);
