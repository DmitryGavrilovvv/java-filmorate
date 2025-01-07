package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.friendship.FriendshipStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendshipRepository extends BaseRepository<User> implements FriendshipStorage {
    private static final String FIND_MUTUAL_QUERY = "select u.* from friends as f1 " +
            "join friends as f2 on f2.friend_id = f1.friend_id " +
            "join users as u on f1.friend_id = u.user_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";
    private static final String FIND_FRIENDS_BY_ID_QUERY = "SELECT * FROM users WHERE user_id IN " +
            "(SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String FIND_FRIEND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id IN " +
            "(SELECT friend_id FROM friends WHERE user_id =? AND friend_id = ?)";
    private static final String INSERT_QUERY = "INSERT INTO friends (user_id, friend_id) SELECT ?,? " +
            "WHERE NOT EXISTS(SELECT * FROM friends WHERE user_id =? AND friend_id = ?)";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbc.update(INSERT_QUERY, userId, friendId, userId, friendId);
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        return jdbc.update(DELETE_QUERY, userId, friendId) > 0 && jdbc.update(DELETE_QUERY, friendId, userId) > 0;
    }

    @Override
    public List<User> getFriends(int userId) {
        return findMany(FIND_FRIENDS_BY_ID_QUERY, userId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int friendId) {
        return findMany(FIND_MUTUAL_QUERY, userId, friendId);
    }

    @Override
    public Optional<User> getFriendById(int userId, int friendId) {
        return findOne(FIND_FRIEND_BY_ID_QUERY, userId, friendId);
    }
}
