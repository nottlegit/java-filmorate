package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmGenreRepository.class, FilmGenreRowMapper.class})
class FilmGenreRepositoryTest {

    @Autowired
    private FilmGenreRepository filmGenreRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM film");
        jdbcTemplate.update("DELETE FROM genre");

        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 10, "Тестовая комедия");
        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 20, "Тестовая драма");
        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 30, "Тестовый боевик");

        jdbcTemplate.update("INSERT INTO film (id, name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                100, "Film One", "First film", "2020-01-01", 120, 1);
        jdbcTemplate.update("INSERT INTO film (id, name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                200, "Film Two", "Second film", "2021-01-01", 90, 1);
    }

    @Test
    void testSaveAndFindAll() {
        filmGenreRepository.save(100L, 10L);
        filmGenreRepository.save(100L, 20L);
        filmGenreRepository.save(200L, 30L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();

        assertThat(allFilmGenres).hasSize(3);

        List<FilmGenre> filmGenresList = List.copyOf(allFilmGenres);

        assertThat(filmGenresList.get(0).getFilmId()).isEqualTo(100L);
        assertThat(filmGenresList.get(0).getGenreId()).isEqualTo(10L);

        assertThat(filmGenresList.get(1).getFilmId()).isEqualTo(100L);
        assertThat(filmGenresList.get(1).getGenreId()).isEqualTo(20L);

        assertThat(filmGenresList.get(2).getFilmId()).isEqualTo(200L);
        assertThat(filmGenresList.get(2).getGenreId()).isEqualTo(30L);
    }

    @Test
    void testSave_Duplicate() {
        filmGenreRepository.save(100L, 10L);

        try {
            filmGenreRepository.save(100L, 10L);
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();
        assertThat(allFilmGenres).hasSize(1);
    }

    @Test
    void testDeleteByFilmId() {
        filmGenreRepository.save(100L, 10L);
        filmGenreRepository.save(100L, 20L);
        filmGenreRepository.save(200L, 30L);

        filmGenreRepository.deleteByFilmId(100L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();

        assertThat(allFilmGenres).hasSize(1);

        FilmGenre remaining = allFilmGenres.iterator().next();
        assertThat(remaining.getFilmId()).isEqualTo(200L);
        assertThat(remaining.getGenreId()).isEqualTo(30L);
    }

    @Test
    void testDeleteByFilmId_EmptyResult() {
        filmGenreRepository.deleteByFilmId(999L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();
        assertThat(allFilmGenres).isEmpty();
    }

    @Test
    void testFindAll_Empty() {
        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();

        assertThat(allFilmGenres).isEmpty();
    }

    @Test
    void testMultipleGenresPerFilm() {
        filmGenreRepository.save(100L, 10L);
        filmGenreRepository.save(100L, 20L);
        filmGenreRepository.save(100L, 30L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();

        assertThat(allFilmGenres).hasSize(3);

        long film100Count = allFilmGenres.stream()
                .filter(fg -> fg.getFilmId() == 100L)
                .count();
        assertThat(film100Count).isEqualTo(3);
    }

    @Test
    void testSameGenreDifferentFilms() {
        filmGenreRepository.save(100L, 10L);
        filmGenreRepository.save(200L, 10L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();

        assertThat(allFilmGenres).hasSize(2);

        long genre10Count = allFilmGenres.stream()
                .filter(fg -> fg.getGenreId() == 10L)
                .count();
        assertThat(genre10Count).isEqualTo(2);
    }

    @Test
    void testFilmGenreProperties() {
        filmGenreRepository.save(100L, 10L);

        Collection<FilmGenre> allFilmGenres = filmGenreRepository.findAll();
        FilmGenre filmGenre = allFilmGenres.iterator().next();

        assertThat(filmGenre.getFilmId()).isEqualTo(100L);
        assertThat(filmGenre.getGenreId()).isEqualTo(10L);
        assertThat(filmGenre.toString()).contains("100").contains("10");
    }
}