package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({MpaRepository.class, MpaRowMapper.class})
class MpaRepositoryTest {

    @Autowired
    private MpaRepository mpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM mpa_rating");

        String insertSql = "INSERT INTO mpa_rating (id, code, name, description) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(insertSql, 1, "G", "General Audiences", "Фильмы для всех возрастов");
        jdbcTemplate.update(insertSql, 2, "PG", "Parental Guidance", "Рекомендуется присутствие родителей");
        jdbcTemplate.update(insertSql, 3, "PG-13", "Parents Strongly Cautioned", "Родительский контроль для детей до 13 лет");
        jdbcTemplate.update(insertSql, 4, "R", "Restricted", "Лицам до 17 лет в сопровождении родителей");
        jdbcTemplate.update(insertSql, 5, "NC-17", "Adults Only", "Только для взрослых");
    }

    @Test
    void testFindAll() {
        List<Mpa> allMpa = mpaRepository.findAll();

        assertThat(allMpa).hasSize(5);

        assertThat(allMpa)
                .extracting(Mpa::getId)
                .containsExactly(1L, 2L, 3L, 4L, 5L);

        allMpa.forEach(mpa -> {
            assertThat(mpa).isNotNull();
            assertThat(mpa.getId()).isNotNull();
            assertThat(mpa.getName()).isNotBlank();
        });
    }

    @Test
    void testFindById_Exists() {
        Optional<Mpa> foundMpa = mpaRepository.findById(3L);

        assertThat(foundMpa).isPresent();
        assertThat(foundMpa.get().getId()).isEqualTo(3L);
        assertThat(foundMpa.get().getName()).isNotNull();
    }

    @Test
    void testFindById_NotExists() {
        Optional<Mpa> foundMpa = mpaRepository.findById(99L);

        assertThat(foundMpa).isEmpty();
    }

    @Test
    void testFindAll_EmptyTable() {
        jdbcTemplate.update("DELETE FROM mpa_rating");

        List<Mpa> allMpa = mpaRepository.findAll();

        assertThat(allMpa).isEmpty();
    }
}