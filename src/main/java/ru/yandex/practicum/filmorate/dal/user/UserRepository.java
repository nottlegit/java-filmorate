package ru.yandex.practicum.filmorate.dal.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM user";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM user WHERE email = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM user WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO user(login, name, email, birthday)" +
            "VALUES (?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE users SET login = ?, name = ?, email = ?," +
            "birthday = ? WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );

        return  User.builder().id(id).build();
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );
        return user;
    }
}
