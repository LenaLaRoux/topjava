DELETE FROM user_role;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date, description, calories)
VALUES (100000, TO_TIMESTAMP('2025-01-31 12:34:33', 'YYYY-MM-DD HH24:MI:SS'), 'Поздний завтрак', 100),
       (100000, TO_TIMESTAMP('2025-01-31 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Обед', 500),
       (100000, TO_TIMESTAMP('2025-01-31 07:016:30', 'YYYY-MM-DD HH24:MI:SS'), 'Завтрак', 300),
       (100000, TO_TIMESTAMP('2025-01-31 20:43:10', 'YYYY-MM-DD HH24:MI:SS'), 'Ужин', 900),
       (100001, TO_TIMESTAMP('2025-01-30 19:00:10', 'YYYY-MM-DD HH24:MI:SS'), 'Ужин', 700),
       (100001, TO_TIMESTAMP('2025-01-30 08:32:17', 'YYYY-MM-DD HH24:MI:SS'), 'Завтрак', 500),
       (100001, TO_TIMESTAMP('2025-01-30 13:15:41', 'YYYY-MM-DD HH24:MI:SS'), 'Обед', 800);
