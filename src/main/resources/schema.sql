-- ===========================================
-- 1. ВОЗРАСТНЫЕ РЕЙТИНГИ MPA
-- ===========================================
CREATE TABLE IF NOT EXISTS mpa_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- ===========================================
-- 2. ЖАНРЫ ФИЛЬМОВ
-- ===========================================
CREATE TABLE IF NOT EXISTS genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ===========================================
-- 3. ПОЛЬЗОВАТЕЛИ
-- ===========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255),
    birthday DATE
);

-- ===========================================
-- 4. ФИЛЬМЫ
-- ===========================================
CREATE TABLE IF NOT EXISTS film (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration BIGINT NOT NULL,
    mpa_rating_id BIGINT NOT NULL,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_rating(id)
);

-- ===========================================
-- 5. СВЯЗЬ ФИЛЬМОВ С ЖАНРАМИ
-- ===========================================
CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);

-- ===========================================
-- 6. ДРУЖБА МЕЖДУ ПОЛЬЗОВАТЕЛЯМИ
-- ===========================================
CREATE TABLE IF NOT EXISTS friendship (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ===========================================
-- 7. ЛАЙКИ ФИЛЬМОВ
-- ===========================================
CREATE TABLE IF NOT EXISTS film_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (film_id, user_id)
);