package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class FilmGenreRepository {

    private final JdbcTemplate jdbc;

    private static final String INSERT_QUERY = """
        INSERT INTO film_genre (film_id, genre_id)\s
        VALUES (?, ?)
       \s""";

    private static final String FIND_ALL_QUERY = """
        SELECT * FROM film_genre\s
        ORDER BY film_id, genre_id
       \s""";

    public FilmGenreRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Long filmId, Long genreId) {
        jdbc.update(INSERT_QUERY, filmId, genreId);
        log.debug("Сохранена связь фильм {} - жанр {}", filmId, genreId);
    }


    public Collection<FilmGenre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, this::mapRowToFilmGenre);
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
}