-- ===========================================
-- 1. ЖАНРЫ (6 записей)
-- ===========================================
-- KEY(name) означает, что H2 будет проверять уникальность по полю name
MERGE INTO genre KEY(name) VALUES (1, 'Комедия');
MERGE INTO genre KEY(name) VALUES (2, 'Драма');
MERGE INTO genre KEY(name) VALUES (3, 'Мультфильм');
MERGE INTO genre KEY(name) VALUES (4, 'Триллер');
MERGE INTO genre KEY(name) VALUES (5, 'Документальный');
MERGE INTO genre KEY(name) VALUES (6, 'Боевик');

-- ===========================================
-- 2. MPA РЕЙТИНГИ (5 записей)
-- ===========================================
MERGE INTO mpa_rating KEY(code) VALUES (1, 'G', 'G', 'General Audiences');
MERGE INTO mpa_rating KEY(code) VALUES (2, 'PG', 'PG', 'Parental Guidance Suggested');
MERGE INTO mpa_rating KEY(code) VALUES (3, 'PG-13', 'PG-13', 'Parents Strongly Cautioned');
MERGE INTO mpa_rating KEY(code) VALUES (4, 'R', 'R', 'Restricted');
MERGE INTO mpa_rating KEY(code) VALUES (5, 'NC-17', 'NC-17', 'Adults Only');

-- ===========================================
-- 3. ПОЛЬЗОВАТЕЛИ (5 записей)
-- ===========================================
MERGE INTO user_table (id, email, login, name, birthday) KEY(id)
VALUES (1, 'ivan@mail.ru', 'ivan_ivanov', 'Иван Иванов', '1990-05-15');

MERGE INTO user_table (id, email, login, name, birthday) KEY(id)
VALUES (2, 'anna@yandex.ru', 'anna_petrova', 'Анна Петрова', '1995-08-22');

MERGE INTO user_table (id, email, login, name, birthday) KEY(id)
VALUES (3, 'sergey@gmail.com', 'sergey_sidorov', 'Сергей Сидоров', '1985-03-10');

MERGE INTO user_table (id, email, login, name, birthday) KEY(id)
VALUES (4, 'olga@mail.ru', 'olga_smirnova', 'Ольга Смирнова', '2000-11-30');

MERGE INTO user_table (id, email, login, name, birthday) KEY(id)
VALUES (5, 'alex@yandex.ru', 'alex_kuznetsov', 'Алексей Кузнецов', '1998-07-05');

-- ===========================================
-- 4. ФИЛЬМЫ (6 записей)
-- ===========================================
MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES ('Матрица', 'Хакер Нео узнает правду о своей реальности', '1999-03-31', 136, 4);

MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES ('Король Лев', 'Молодой львёнок Симба учится ответственности', '1994-06-15', 88, 1);

MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES (
    'Побег из Шоушенка', 'Банкир Энди Дюфресн приговорен к пожизненному заключению в Шоушенке', '1994-09-23', 142, 4
);

MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES ('Назад в будущее', 'Подросток Марти Макфлай отправляется в прошлое', '1985-07-03', 116, 2);

MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES ('Титаник', 'Роман о печально известном корабле', '1997-12-19', 195, 3);

MERGE INTO film (name, description, release_date, duration, mpa_rating_id) KEY(name, release_date)
VALUES ('Пираты Карибского моря', 'Приключения капитана Джека Воробья', '2003-07-09', 143, 3);

-- ===========================================
-- 5. СВЯЗЬ ФИЛЬМОВ С ЖАНРАМИ
-- ===========================================
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (1, 6); -- Матрица: Боевик
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (1, 4); -- Матрица: Триллер
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (2, 3); -- Король Лев: Мультфильм
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (2, 2); -- Король Лев: Драма
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (3, 2); -- Побег из Шоушенка: Драма
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (4, 1); -- Назад в будущее: Комедия
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (4, 6); -- Назад в будущее: Боевик
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (5, 2); -- Титаник: Драма
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (6, 6); -- Пираты: Боевик
MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (6, 1); -- Пираты: Комедия

-- ===========================================
-- 6. ДРУЖБА
-- ===========================================
MERGE INTO friendship (user_id, friend_id, status) KEY(user_id, friend_id)
VALUES (1, 2, 'confirmed');

MERGE INTO friendship (user_id, friend_id, status) KEY(user_id, friend_id)
VALUES (1, 3, 'pending');

MERGE INTO friendship (user_id, friend_id, status) KEY(user_id, friend_id)
VALUES (2, 4, 'confirmed');

MERGE INTO friendship (user_id, friend_id, status) KEY(user_id, friend_id)
VALUES (3, 5, 'confirmed');

-- ===========================================
-- 7. ЛАЙКИ
-- ===========================================
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (1, 1);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (2, 1);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (5, 2);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (6, 2);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (3, 3);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (4, 4);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (1, 4);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (1, 5);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (2, 5);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (3, 5);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (4, 5);
MERGE INTO film_like (film_id, user_id) KEY(film_id, user_id) VALUES (6, 5);