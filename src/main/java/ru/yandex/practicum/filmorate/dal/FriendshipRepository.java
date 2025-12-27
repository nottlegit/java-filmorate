package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {
    private static final String INSERT_QUERY = "INSERT INTO friendship(user_id, friend_id, status)" +
            "VALUES (?, ?, ?)";
    private static final String FIND_BY_IDS_QUERY =
            "SELECT * FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_BY_IDS_QUERY =
            "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_BY_USER_ID_QUERY =
            "SELECT * FROM friendship WHERE user_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public Friendship save(Friendship friendship) {
        update(
                INSERT_QUERY,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus().getValue()
        );
        return friendship;
    }

    public boolean deleteByIds(long userId, long friendId) {
        return delete(DELETE_BY_IDS_QUERY, userId, friendId);
    }

    public Optional<Friendship> findByIds(long userId, long friendId) {
        return findOne(FIND_BY_IDS_QUERY, userId, friendId);
    }

    public Collection<Friendship> findByUserId(long userId) {
        return findMany(FIND_BY_USER_ID_QUERY, userId);
    }
}