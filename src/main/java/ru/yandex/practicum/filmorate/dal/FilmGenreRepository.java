package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class FilmGenreRepository {
    private final JdbcTemplate jdbc;
    private final FilmGenreRowMapper filmGenreRowMapper;

    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre ORDER BY film_id, genre_id";

    public FilmGenreRepository(JdbcTemplate jdbc, FilmGenreRowMapper filmGenreRowMapper) {
        this.jdbc = jdbc;
        this.filmGenreRowMapper = filmGenreRowMapper;
    }

    public void save(Long filmId, Long genreId) {
        jdbc.update(INSERT_QUERY, filmId, genreId);
    }

    public void batchSave(long filmId, Collection<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;

        List<Genre> genreList = new ArrayList<>(genres);
        jdbc.batchUpdate(INSERT_QUERY, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
            @Override
            public void setValues(java.sql.PreparedStatement ps, int i) throws java.sql.SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genreList.get(i).getId());
            }
            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
        log.debug("Сохранены жанры для фильма {}", filmId);
    }

    public Collection<FilmGenre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, filmGenreRowMapper);
    }

    public void deleteByFilmId(long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbc.update(sql, filmId);
    }
}