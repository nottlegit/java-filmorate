package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.FilmLikeRowMapper;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmLikeRepository.class, FilmLikeRowMapper.class})
class FilmLikeRepositoryTest {

    @Autowired
    private FilmLikeRepository filmLikeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM film_like");
        jdbcTemplate.update("DELETE FROM film");
        jdbcTemplate.update("DELETE FROM users");

        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                100, "user1@mail.ru", "user1", "User One");
        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                200, "user2@mail.ru", "user2", "User Two");
        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                300, "user3@mail.ru", "user3", "User Three");

        jdbcTemplate.update("INSERT INTO film (id, name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                100, "Film One", "First film", "2020-01-01", 120, 1);
        jdbcTemplate.update("INSERT INTO film (id, name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                200, "Film Two", "Second film", "2021-01-01", 90, 1);
        jdbcTemplate.update("INSERT INTO film (id, name, description, release_date, duration, mpa_rating_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                300, "Film Three", "Third film", "2022-01-01", 150, 1);
    }

    @Test
    void testSaveAndFindByFilmIdAndUserId() {
        FilmLike filmLike = FilmLike.builder()
                .filmId(100L)
                .userId(200L)
                .build();

        FilmLike saved = filmLikeRepository.save(filmLike);

        assertThat(saved.getId()).isNotNull().isPositive();

        Optional<FilmLike> found = filmLikeRepository.findByFilmIdAndUserId(100L, 200L);

        assertThat(found).isPresent();
        assertThat(found.get().getFilmId()).isEqualTo(100L);
        assertThat(found.get().getUserId()).isEqualTo(200L);
    }

    @Test
    void testFindByFilmIdAndUserId_NotExists() {
        Optional<FilmLike> found = filmLikeRepository.findByFilmIdAndUserId(999L, 999L);

        assertThat(found).isEmpty();
    }

    @Test
    void testDeleteByFilmIdAndUserId() {
        FilmLike filmLike = FilmLike.builder()
                .filmId(100L)
                .userId(200L)
                .build();

        FilmLike saved = filmLikeRepository.save(filmLike);

        boolean deleted = filmLikeRepository.deleteByFilmIdAndUserId(saved);

        assertThat(deleted).isTrue();

        Optional<FilmLike> found = filmLikeRepository.findByFilmIdAndUserId(100L, 200L);
        assertThat(found).isEmpty();
    }

    @Test
    void testDeleteByFilmIdAndUserId_NotExists() {
        FilmLike filmLike = FilmLike.builder()
                .filmId(999L)
                .userId(888L)
                .build();

        boolean deleted = filmLikeRepository.deleteByFilmIdAndUserId(filmLike);

        assertThat(deleted).isFalse();
    }

    @Test
    void testFindTopFilmsByLikes() {
        filmLikeRepository.save(FilmLike.builder().filmId(100L).userId(100L).build());
        filmLikeRepository.save(FilmLike.builder().filmId(100L).userId(200L).build());
        filmLikeRepository.save(FilmLike.builder().filmId(100L).userId(300L).build());

        filmLikeRepository.save(FilmLike.builder().filmId(200L).userId(100L).build());
        filmLikeRepository.save(FilmLike.builder().filmId(200L).userId(200L).build());

        filmLikeRepository.save(FilmLike.builder().filmId(300L).userId(100L).build());

        Collection<Long> topFilms = filmLikeRepository.findTopFilmsByLikes(2);

        assertThat(topFilms).hasSize(2);
        assertThat(topFilms).containsExactly(100L, 200L);
    }

    @Test
    void testFindTopFilmsByLikes_WithTies() {
        filmLikeRepository.save(FilmLike.builder().filmId(100L).userId(100L).build());
        filmLikeRepository.save(FilmLike.builder().filmId(200L).userId(100L).build());
        filmLikeRepository.save(FilmLike.builder().filmId(300L).userId(100L).build());

        Collection<Long> topFilms = filmLikeRepository.findTopFilmsByLikes(2);

        assertThat(topFilms).hasSize(2);
        assertThat(topFilms).containsExactly(100L, 200L);
    }
}