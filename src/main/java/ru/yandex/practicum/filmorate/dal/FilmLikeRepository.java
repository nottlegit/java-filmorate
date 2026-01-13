package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {

    private static final String INSERT_QUERY = """
             INSERT INTO film_like (film_id, user_id)\s
             VALUES (?, ?)
            \s""";
    private static final String FIND_BY_FILM_AND_USER_QUERY = """
             SELECT * FROM film_like\s
             WHERE film_id = ? AND user_id = ?
            \s""";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_like ORDER BY created_at DESC";
    private static final String DELETE_BY_FILM_AND_USER_QUERY = """
             DELETE FROM film_like\s
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

    public FilmLike save(FilmLike filmLike) {
        log.debug("Сохранение лайка: {}", filmLike);
        long id = insert(INSERT_QUERY,
                filmLike.getFilmId(),
                filmLike.getUserId()
        );
        return filmLike.toBuilder().id(id).build();
    }

    public Optional<FilmLike> findByFilmIdAndUserId(Long filmId, Long userId) {
        log.debug("Поиск лайка по filmId={}, userId={}", filmId, userId);
        return findOne(FIND_BY_FILM_AND_USER_QUERY, filmId, userId);
    }

    public List<FilmLike> findAll() {
        log.debug("Получение всех лайков");
        return findMany(FIND_ALL_QUERY);
    }

    public boolean deleteByFilmIdAndUserId(FilmLike filmLike) {
        log.debug("Удаление лайка по filmId={}, userId={}", filmLike.getFilmId(), filmLike.getUserId());
        return delete(DELETE_BY_FILM_AND_USER_QUERY, filmLike.getFilmId(), filmLike.getUserId());
    }

    public Collection<Long> findTopFilmsByLikes(int limit) {
        log.debug("Поиск топ-{} фильмов по лайкам", limit);
        return jdbc.query(FIND_TOP_FILMS_QUERY,
                (rs, rowNum) -> rs.getLong("film_id"),
                limit
        );
    }
}