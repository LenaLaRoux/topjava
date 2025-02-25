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

INSERT INTO meals (user_id, meal_time, description, calories)
VALUES (100000, '2025-01-31 12:34', 'Поздний завтрак user', 100),
       (100000, '2025-01-31 15:00', 'Обед user', 500),
       (100000, '2025-01-31 07:16', 'Завтрак user', 300),
       (100000, '2025-01-31 20:43', 'Ужин user', 900),
       (100001, '2025-01-30 19:00', 'Ужин admin', 700),
       (100001, '2025-01-30 08:32', 'Завтрак admin', 500),
       (100001, '2025-01-30 13:15', 'Обед admin', 800);
