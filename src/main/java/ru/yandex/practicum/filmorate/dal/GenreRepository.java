package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository {

    private final JdbcTemplate jdbc;

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, (rs, rowNum) ->
                Genre.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    public Optional<Genre> findById(Long id) {
        List<Genre> genres = jdbc.query(FIND_BY_ID_QUERY,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .build(),
                id
        );
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.getFirst());
    }
}