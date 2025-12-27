package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipStatus;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FriendshipRepository.class, FriendshipRowMapper.class})
class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM friendship");
        jdbcTemplate.update("DELETE FROM users");

        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                1, "user1@mail.ru", "user1", "User One");
        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                2, "user2@mail.ru", "user2", "User Two");
        jdbcTemplate.update("INSERT INTO users (id, email, login, name) VALUES (?, ?, ?, ?)",
                3, "user3@mail.ru", "user3", "User Three");
    }

    @Test
    void testSaveAndFindByIds() {
        Friendship friendship = Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .status(FriendshipStatus.PENDING)
                .build();

        Friendship saved = friendshipRepository.save(friendship);

        Optional<Friendship> found = friendshipRepository.findByIds(1L, 2L);

        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(1L);
        assertThat(found.get().getFriendId()).isEqualTo(2L);
        assertThat(found.get().getStatus()).isEqualTo(FriendshipStatus.PENDING);
    }

    @Test
    void testFindByIds_NotExists() {
        Optional<Friendship> found = friendshipRepository.findByIds(1L, 999L);

        assertThat(found).isEmpty();
    }

    @Test
    void testDeleteByIds() {
        friendshipRepository.save(Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .status(FriendshipStatus.PENDING)
                .build());

        boolean deleted = friendshipRepository.deleteByIds(1L, 2L);

        assertThat(deleted).isTrue();

        Optional<Friendship> found = friendshipRepository.findByIds(1L, 2L);
        assertThat(found).isEmpty();
    }

    @Test
    void testDeleteByIds_NotExists() {
        boolean deleted = friendshipRepository.deleteByIds(999L, 888L);

        assertThat(deleted).isFalse();
    }

    @Test
    void testFindByUserId() {
        friendshipRepository.save(Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .status(FriendshipStatus.PENDING)
                .build());

        friendshipRepository.save(Friendship.builder()
                .userId(1L)
                .friendId(3L)
                .status(FriendshipStatus.CONFIRMED)
                .build());

        friendshipRepository.save(Friendship.builder()
                .userId(2L)
                .friendId(3L)
                .status(FriendshipStatus.PENDING)
                .build());

        Collection<Friendship> friendships = friendshipRepository.findByUserId(1L);

        assertThat(friendships).hasSize(2);
        assertThat(friendships)
                .extracting(Friendship::getFriendId)
                .containsExactlyInAnyOrder(2L, 3L);

        assertThat(friendships)
                .extracting(Friendship::getStatus)
                .containsExactlyInAnyOrder(FriendshipStatus.PENDING, FriendshipStatus.CONFIRMED);
    }

    @Test
    void testFindByUserId_NoFriends() {
        Collection<Friendship> friendships = friendshipRepository.findByUserId(1L);

        assertThat(friendships).isEmpty();
    }

    @Test
    void testSaveWithDifferentStatuses() {
        friendshipRepository.save(Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .status(FriendshipStatus.PENDING)
                .build());

        friendshipRepository.save(Friendship.builder()
                .userId(2L)
                .friendId(3L)
                .status(FriendshipStatus.CONFIRMED)
                .build());

        Optional<Friendship> pending = friendshipRepository.findByIds(1L, 2L);
        Optional<Friendship> confirmed = friendshipRepository.findByIds(2L, 3L);

        assertThat(pending).isPresent();
        assertThat(pending.get().getStatus()).isEqualTo(FriendshipStatus.PENDING);

        assertThat(confirmed).isPresent();
        assertThat(confirmed.get().getStatus()).isEqualTo(FriendshipStatus.CONFIRMED);
    }
}