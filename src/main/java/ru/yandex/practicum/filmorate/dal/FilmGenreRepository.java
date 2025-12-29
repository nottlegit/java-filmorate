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
import java.util.stream.Collectors;

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

        List<Object[]> batchArgs = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .collect(Collectors.toList());

        jdbc.batchUpdate(INSERT_QUERY, batchArgs);

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