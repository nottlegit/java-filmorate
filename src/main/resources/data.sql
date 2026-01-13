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