package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

@Slf4j
@Repository
public class FilmGenreRepository {

    private final JdbcTemplate jdbc;
    private final FilmGenreRowMapper filmGenreRowMapper; // Добавляем поле

    private static final String INSERT_QUERY = """
             INSERT INTO film_genre (film_id, genre_id)
             VALUES (?, ?)
            """;

    private static final String FIND_ALL_QUERY = """
             SELECT * FROM film_genre
             ORDER BY film_id, genre_id
            """;

    public FilmGenreRepository(JdbcTemplate jdbc, FilmGenreRowMapper filmGenreRowMapper) {
        this.jdbc = jdbc;
        this.filmGenreRowMapper = filmGenreRowMapper;
    }

    public void save(Long filmId, Long genreId) {
        jdbc.update(INSERT_QUERY, filmId, genreId);
        log.debug("Сохранена связь фильм {} - жанр {}", filmId, genreId);
    }

    public Collection<FilmGenre> findAll() {
        // Теперь используем внедренный маппер
        return jdbc.query(FIND_ALL_QUERY, filmGenreRowMapper);
    }

    public void deleteByFilmId(long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbc.update(sql, filmId);
    }
}