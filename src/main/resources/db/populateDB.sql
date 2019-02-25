DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
   (100000, '2019-02-24 7:00', 'Завтрак', 500),
   (100000, '2019-02-24 13:00', 'Обед', 1000),
   (100000, '2019-02-24 20:00', 'Ужин', 510),
   (100001, '2019-02-25 8:00', 'Завтрак админа', 500),
   (100001, '2019-02-25 12:30', 'Обед админа', 1000),
   (100001, '2019-02-25 19:00', 'Ужин админа', 500);