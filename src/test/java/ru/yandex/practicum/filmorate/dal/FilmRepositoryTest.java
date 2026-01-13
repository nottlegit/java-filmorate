package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmRepository.class, FilmRowMapper.class})
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM film");
        jdbcTemplate.update("DELETE FROM mpa_rating");
        jdbcTemplate.update("DELETE FROM genre");

        jdbcTemplate.update("INSERT INTO mpa_rating (id, code, name, description) VALUES (?, ?, ?, ?)",
                1, "G", "G", "General Audiences");
        jdbcTemplate.update("INSERT INTO mpa_rating (id, code, name, description) VALUES (?, ?, ?, ?)",
                2, "PG", "PG", "Parental Guidance");

        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 1, "Комедия");
        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 2, "Драма");
        jdbcTemplate.update("INSERT INTO genre (id, name) VALUES (?, ?)", 3, "Боевик");
    }

    @Test
    void testSaveAndFindById() {
        Mpa mpa = Mpa.builder().id(1L).name("G").build();
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2020, 5, 15))
                .duration(120)
                .mpa(mpa)
                .build();

        Film savedFilm = filmRepository.save(film);

        assertThat(savedFilm.getId()).isNotNull().isPositive();

        Optional<Film> foundFilm = filmRepository.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getName()).isEqualTo("Test Film");
        assertThat(foundFilm.get().getDescription()).isEqualTo("Test Description");
        assertThat(foundFilm.get().getReleaseDate()).isEqualTo(LocalDate.of(2020, 5, 15));
        assertThat(foundFilm.get().getDuration()).isEqualTo(120);
        assertThat(foundFilm.get().getMpa().getId()).isEqualTo(1L);
        assertThat(foundFilm.get().getGenres()).isEmpty();
    }

    @Test
    void testFindById_WithGenres() {
        Mpa mpa = Mpa.builder().id(1L).name("G").build();
        Film film = Film.builder()
                .name("Film with Genres")
                .description("Description")
                .releaseDate(LocalDate.of(2021, 6, 20))
                .duration(90)
                .mpa(mpa)
                .build();

        Film savedFilm = filmRepository.save(film);

        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", savedFilm.getId(), 1);
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", savedFilm.getId(), 3);

        Optional<Film> foundFilm = filmRepository.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getGenres()).hasSize(2);
        assertThat(foundFilm.get().getGenres())
                .extracting(Genre::getId)
                .containsExactly(1L, 3L);
        assertThat(foundFilm.get().getGenres())
                .extracting(Genre::getName)
                .containsExactly("Комедия", "Боевик");
    }

    @Test
    void testUpdate() {
        Mpa mpa1 = Mpa.builder().id(1L).name("G").build();
        Mpa mpa2 = Mpa.builder().id(2L).name("PG").build();

        Film film = Film.builder()
                .name("Original Film")
                .description("Original Description")
                .releaseDate(LocalDate.of(2019, 3, 10))
                .duration(100)
                .mpa(mpa1)
                .build();

        Film savedFilm = filmRepository.save(film);

        Film updatedFilm = savedFilm.toBuilder()
                .name("Updated Film")
                .description("Updated Description")
                .duration(150)
                .mpa(mpa2)
                .releaseDate(LocalDate.of(2022, 8, 25))
                .build();

        filmRepository.update(updatedFilm);

        Optional<Film> foundFilm = filmRepository.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getName()).isEqualTo("Updated Film");
        assertThat(foundFilm.get().getDescription()).isEqualTo("Updated Description");
        assertThat(foundFilm.get().getDuration()).isEqualTo(150);
        assertThat(foundFilm.get().getMpa().getId()).isEqualTo(2L);
        assertThat(foundFilm.get().getReleaseDate()).isEqualTo(LocalDate.of(2022, 8, 25));
    }

    @Test
    void testFindAll() {
        Mpa mpa = Mpa.builder().id(1L).name("G").build();

        Film film1 = Film.builder()
                .name("Film One")
                .description("First film")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(90)
                .mpa(mpa)
                .build();

        Film film2 = Film.builder()
                .name("Film Two")
                .description("Second film")
                .releaseDate(LocalDate.of(2021, 2, 2))
                .duration(120)
                .mpa(mpa)
                .build();

        Film saved1 = filmRepository.save(film1);
        Film saved2 = filmRepository.save(film2);

        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", saved1.getId(), 1);
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", saved2.getId(), 2);
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", saved2.getId(), 3);

        Collection<Film> allFilms = filmRepository.findAll();

        assertThat(allFilms).hasSize(2);

        long saved1Id = saved1.getId();
        long saved2Id = saved2.getId();

        Film foundFilm1 = allFilms.stream()
                .filter(f -> f.getId() == saved1Id)
                .findFirst()
                .orElseThrow();

        Film foundFilm2 = allFilms.stream()
                .filter(f -> f.getId() == saved2Id)
                .findFirst()
                .orElseThrow();

        assertThat(foundFilm1.getGenres()).hasSize(1);
        assertThat(foundFilm1.getGenres())
                .extracting(Genre::getId)
                .containsExactly(1L);

        assertThat(foundFilm2.getGenres()).hasSize(2);
        assertThat(foundFilm2.getGenres())
                .extracting(Genre::getId)
                .containsExactly(2L, 3L);
    }

    @Test
    void testFindById_NotExists() {
        Optional<Film> foundFilm = filmRepository.findById(999L);

        assertThat(foundFilm).isEmpty();
    }

    @Test
    void testFilmWithAllFields() {
        Mpa mpa = Mpa.builder().id(1L).name("G").build();
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(Genre.builder().id(1L).name("Комедия").build());
        genres.add(Genre.builder().id(2L).name("Драма").build());

        Film film = Film.builder()
                .name("Complete Film")
                .description("A film with all details")
                .releaseDate(LocalDate.of(2023, 12, 25))
                .duration(180)
                .mpa(mpa)
                .genres(genres)
                .build();

        Film savedFilm = filmRepository.save(film);

        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                    savedFilm.getId(), genre.getId());
        }

        Optional<Film> foundFilm = filmRepository.findById(savedFilm.getId());

        assertThat(foundFilm).isPresent();
        assertThat(foundFilm.get().getGenres()).hasSize(2);
    }
}