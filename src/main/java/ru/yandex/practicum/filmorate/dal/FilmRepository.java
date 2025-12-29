package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name
            FROM film f
            LEFT JOIN mpa_rating m ON f.mpa_rating_id = m.id
            """;

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE f.id = ?";

    private static final String INSERT_QUERY = "INSERT INTO film(name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? WHERE id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> findAll() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        loadGenresForFilms(films);
        return films;
    }

    public Optional<Film> findById(long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        film.ifPresent(f -> loadGenresForFilms(List.of(f)));
        return film;
    }

    public List<Film> findByIds(Collection<Long> ids) {
        if (ids.isEmpty()) return Collections.emptyList();
        String inSql = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        String query = FIND_ALL_QUERY + " WHERE f.id IN (" + inSql + ")";
        Collection<Film> films = findMany(query);
        loadGenresForFilms(films);
        return new ArrayList<>(films);
    }

    public Film save(Film film) {
        long id = insert(INSERT_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return film.toBuilder().id(id).build();
    }

    public void update(Film film) {
        update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
    }

    private void loadGenresForFilms(Collection<Film> films) {
        if (films.isEmpty()) return;

        List<Long> filmIds = films.stream().map(Film::getId).toList();
        String inSql = filmIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String genresSql = """
                SELECT fg.film_id, g.id AS genre_id, g.name AS genre_name
                FROM genre g
                JOIN film_genre fg ON g.id = fg.genre_id
                WHERE fg.film_id IN (%s)
                ORDER BY g.id
                """.formatted(inSql);

        Map<Long, List<Genre>> genresByFilmId = jdbc.query(genresSql, rs -> {
            Map<Long, List<Genre>> result = new HashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                result.computeIfAbsent(filmId, k -> new ArrayList<>()).add(
                        Genre.builder().id(rs.getLong("genre_id")).name(rs.getString("genre_name")).build()
                );
            }
            return result;
        });

        films.forEach(f -> f.setGenres(new LinkedHashSet<>(genresByFilmId.getOrDefault(f.getId(), List.of()))));
    }
}