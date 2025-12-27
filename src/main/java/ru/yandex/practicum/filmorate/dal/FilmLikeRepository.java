package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {

    // SQL запросы вынесены в константы
    private static final String INSERT_QUERY = """
        INSERT INTO film_like (film_id, user_id)\s
        VALUES (?, ?)
       \s""";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_like WHERE id = ?";
    private static final String FIND_BY_FILM_AND_USER_QUERY = """
        SELECT * FROM film_like\s
        WHERE film_id = ? AND user_id = ?
       \s""";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM film_like WHERE film_id = ?";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM film_like WHERE user_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_like ORDER BY created_at DESC";
    private static final String DELETE_BY_FILM_AND_USER_QUERY = """
        DELETE FROM film_like\s
        WHERE film_id = ? AND user_id = ?
       \s""";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM film_like WHERE id = ?";
    private static final String EXISTS_BY_FILM_AND_USER_QUERY = """
        SELECT COUNT(*) > 0 FROM film_like\s
        WHERE film_id = ? AND user_id = ?
       \s""";
    private static final String FIND_TOP_FILMS_QUERY = """
            SELECT f.id as film_id, COUNT(fl.user_id) as like_count
                FROM film f
                LEFT JOIN film_like fl ON f.id = fl.film_id
                GROUP BY f.id
                ORDER BY like_count DESC, f.id ASC
                LIMIT ?
            """;

    public FilmLikeRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmLike> filmLikeRowMapper) {
        super(jdbcTemplate, filmLikeRowMapper);
    }

    /**
     * Сохранить лайк
     */
    public FilmLike save(FilmLike filmLike) {
        log.debug("Сохранение лайка: {}", filmLike);
        long id = insert(INSERT_QUERY,
                filmLike.getFilmId(),
                filmLike.getUserId()
        );
        return filmLike.toBuilder().id(id).build();
    }

    /**
     * Найти лайк по ID
     */
    public Optional<FilmLike> findById(Long id) {
        log.debug("Поиск лайка по ID: {}", id);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    /**
     * Найти лайк по фильму и пользователю
     */
    public Optional<FilmLike> findByFilmIdAndUserId(Long filmId, Long userId) {
        log.debug("Поиск лайка по filmId={}, userId={}", filmId, userId);
        return findOne(FIND_BY_FILM_AND_USER_QUERY, filmId, userId);
    }

    /**
     * Найти все лайки фильма
     */
    public List<FilmLike> findByFilmId(Long filmId) {
        log.debug("Поиск лайков по filmId={}", filmId);
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    /**
     * Найти все лайки пользователя
     */
    public List<FilmLike> findByUserId(Long userId) {
        log.debug("Поиск лайков по userId={}", userId);
        return findMany(FIND_BY_USER_ID_QUERY, userId);
    }

    /**
     * Получить все лайки
     */
    public List<FilmLike> findAll() {
        log.debug("Получение всех лайков");
        return findMany(FIND_ALL_QUERY);
    }

    /**
     * Удалить лайк по ID
     */
    public boolean deleteById(Long id) {
        log.debug("Удаление лайка по ID: {}", id);
        return delete(DELETE_BY_ID_QUERY, id);
    }

    /**
     * Удалить лайк по фильму и пользователю
     */
    public boolean deleteByFilmIdAndUserId(FilmLike filmLike) {
        log.debug("Удаление лайка по filmId={}, userId={}", filmLike.getFilmId(), filmLike.getUserId());
        return delete(DELETE_BY_FILM_AND_USER_QUERY, filmLike.getFilmId(), filmLike.getUserId());
    }

    /**
     * Проверить, существует ли лайк
     */
    public boolean existsByFilmIdAndUserId(Long filmId, Long userId) {
        log.debug("Проверка существования лайка filmId={}, userId={}", filmId, userId);
        Boolean exists = jdbc.queryForObject(
                EXISTS_BY_FILM_AND_USER_QUERY, Boolean.class, filmId, userId
        );
        return exists != null && exists;
    }

    /**
     * Получить топ-N фильмов по лайкам
     */
    public Collection<Long> findTopFilmsByLikes(int limit) {
        log.debug("Поиск топ-{} фильмов по лайкам", limit);
        return jdbc.query(FIND_TOP_FILMS_QUERY,
                (rs, rowNum) -> rs.getLong("film_id"),
                limit
        );
    }
}