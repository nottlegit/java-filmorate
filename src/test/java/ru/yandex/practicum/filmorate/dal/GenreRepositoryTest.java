package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreRepository.class, GenreRowMapper.class})
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM genre");

        String insertSql = "INSERT INTO genre (id, name) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, 1, "Комедия");
        jdbcTemplate.update(insertSql, 2, "Драма");
        jdbcTemplate.update(insertSql, 3, "Мультфильм");
        jdbcTemplate.update(insertSql, 4, "Триллер");
        jdbcTemplate.update(insertSql, 5, "Документальный");
        jdbcTemplate.update(insertSql, 6, "Боевик");
    }

    @Test
    void testFindAll() {
        Collection<Genre> allGenres = genreRepository.findAll();

        assertThat(allGenres).hasSize(6);
        assertThat(allGenres)
                .extracting(Genre::getName)
                .containsExactly("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");

        assertThat(allGenres)
                .extracting(Genre::getId)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }

    @Test
    void testFindById_Exists() {
        Optional<Genre> foundGenre = genreRepository.findById(3L);

        assertThat(foundGenre).isPresent();
        assertThat(foundGenre.get().getId()).isEqualTo(3L);
        assertThat(foundGenre.get().getName()).isEqualTo("Мультфильм");
    }

    @Test
    void testFindById_NotExists() {
        Optional<Genre> foundGenre = genreRepository.findById(99L);

        assertThat(foundGenre).isEmpty();
    }

    @Test
    void testFindAll_EmptyTable() {
        jdbcTemplate.update("DELETE FROM genre");

        Collection<Genre> allGenres = genreRepository.findAll();

        assertThat(allGenres).isEmpty();
    }

    @Test
    void testGenreProperties() {
        Optional<Genre> genre = genreRepository.findById(1L);

        assertThat(genre).isPresent();
        assertThat(genre.get().getId()).isEqualTo(1L);
        assertThat(genre.get().getName()).isEqualTo("Комедия");
        assertThat(genre.get().toString()).contains("Комедия");
    }
}