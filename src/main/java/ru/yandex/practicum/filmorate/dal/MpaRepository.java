package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL_SQL = "SELECT * FROM mpa_rating ORDER BY id";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM mpa_rating WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mpaRowMapper) {
        super(jdbcTemplate, mpaRowMapper);
    }

    public List<Mpa> findAll() {
        log.debug("Получение всех MPA рейтингов");
        return findMany(FIND_ALL_SQL);
    }

    public Optional<Mpa> findById(Long id) {
        log.debug("Поиск MPA рейтинга по ID: {}", id);
        return findOne(FIND_BY_ID_SQL, id);
    }
}
