package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = """
            SELECT f.*,\s
                   m.id AS mpa_id,\s
                   m.name AS mpa_name
            FROM film f
            LEFT JOIN mpa_rating m ON f.mpa_rating_id = m.id
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT f.*,\s
                   m.id AS mpa_id,\s
                   m.name AS mpa_name
            FROM film f
            LEFT JOIN mpa_rating m ON f.mpa_rating_id = m.id
            WHERE f.id = ?
            """;
    private static final String INSERT_QUERY =
            "INSERT INTO film(name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?," +
            "duration = ?, mpa_rating_id = ? WHERE id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );

        return film.toBuilder().id(id).build();
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }
}
