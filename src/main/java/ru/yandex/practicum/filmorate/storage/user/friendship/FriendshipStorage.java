package ru.yandex.practicum.filmorate.storage.user.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getMutualFriends(int userId, int friendId);

    Optional<User> getFriendById(int userId, int friendId);
}
