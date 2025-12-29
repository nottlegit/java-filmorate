package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({UserRepository.class, UserRowMapper.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private User testUser;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users");

        testUser = User.builder()
                .login("testLogin")
                .name("Test User")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void testSaveAndFindById() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull().isPositive();

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@mail.ru");
        assertThat(foundUser.get().getLogin()).isEqualTo("testLogin");
    }

    @Test
    void testFindByEmail() {
        User savedUser = userRepository.save(testUser);

        Optional<User> foundUser = userRepository.findByEmail("test@mail.ru");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());

        Optional<User> notFound = userRepository.findByEmail("wrong@mail.ru");
        assertThat(notFound).isEmpty();
    }

    @Test
    void testFindAll() {
        User user1 = userRepository.save(testUser);

        User user2 = User.builder()
                .login("anotherLogin")
                .name("Another User")
                .email("another@mail.ru")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2);
        assertThat(allUsers)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder("test@mail.ru", "another@mail.ru");
    }

    @Test
    void testUpdate() {
        User savedUser = userRepository.save(testUser);

        User updatedUser = savedUser.toBuilder()
                .name("Updated Name")
                .email("updated@mail.ru")
                .build();

        userRepository.update(updatedUser);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Updated Name");
        assertThat(foundUser.get().getEmail()).isEqualTo("updated@mail.ru");
        assertThat(foundUser.get().getLogin()).isEqualTo("testLogin");
    }

    @Test
    void testFindById_NotFound() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertThat(foundUser).isEmpty();
    }
}