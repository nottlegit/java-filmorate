package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmGenreRepository {

    private final JdbcTemplate jdbc;

    private static final String INSERT_QUERY = """
        INSERT INTO film_genre (film_id, genre_id)\s
        VALUES (?, ?)
       \s""";

    private static final String DELETE_BY_FILM_QUERY = """
        DELETE FROM film_genre\s
        WHERE film_id = ?
       \s""";

    private static final String FIND_GENRE_IDS_BY_FILM_QUERY = """
        SELECT genre_id FROM film_genre\s
        WHERE film_id = ?\s
        ORDER BY genre_id
       \s""";

    private static final String FIND_ALL_QUERY = """
        SELECT * FROM film_genre\s
        ORDER BY film_id, genre_id
       \s""";

    private static final String FIND_BY_ID_QUERY = """
        SELECT * FROM film_genre\s
        WHERE film_id = ? AND genre_id = ?
       \s""";

    public FilmGenreRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Long filmId, Long genreId) {
        jdbc.update(INSERT_QUERY, filmId, genreId);
        log.debug("Сохранена связь фильм {} - жанр {}", filmId, genreId);
    }

    public void deleteByFilmId(Long filmId) {
        jdbc.update(DELETE_BY_FILM_QUERY, filmId);
        log.debug("Удалены все жанры у фильма {}", filmId);
    }

    public List<Long> findGenreIdsByFilmId(Long filmId) {
        return jdbc.query(FIND_GENRE_IDS_BY_FILM_QUERY,
                (rs, rowNum) -> rs.getLong("genre_id"),
                filmId
        );
    }

    public Collection<FilmGenre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, this::mapRowToFilmGenre);
    }

    public Optional<FilmGenre> findById(Long filmId, Long genreId) {
        List<FilmGenre> result = jdbc.query(FIND_BY_ID_QUERY,
                this::mapRowToFilmGenre,
                filmId, genreId);

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private FilmGenre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .filmId(rs.getLong("film_id"))
                .genreId(rs.getLong("genre_id"))
                .build();
    }

    public List<FilmGenre> findByFilmId(Long filmId) {
        String query = "SELECT * FROM film_genre WHERE film_id = ? ORDER BY genre_id";
        return jdbc.query(query, this::mapRowToFilmGenre, filmId);
    }


    public List<FilmGenre> findByGenreId(Long genreId) {
        String query = "SELECT * FROM film_genre WHERE genre_id = ? ORDER BY film_id";
        return jdbc.query(query, this::mapRowToFilmGenre, genreId);
    }

    public boolean exists(Long filmId, Long genreId) {
        String query = "SELECT COUNT(*) FROM film_genre WHERE film_id = ? AND genre_id = ?";
        Integer count = jdbc.queryForObject(query, Integer.class, filmId, genreId);
        return count != null && count > 0;
    }

    public boolean delete(Long filmId, Long genreId) {
        String query = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
        int rowsDeleted = jdbc.update(query, filmId, genreId);
        boolean deleted = rowsDeleted > 0;
        if (deleted) {
            log.debug("Удалена связь фильм {} - жанр {}", filmId, genreId);
        }
        return deleted;
    }
}