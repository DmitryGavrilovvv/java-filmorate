package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User create(User user) {
        user.setId(++idGenerator);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.error("Пользователь с id {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        log.error("Ошибка при получении списка юзеров: пользователь не найден");
        return Optional.empty();
    }

    public Optional<List<User>> getFriends(Integer id) {
        List<User> result = new ArrayList<>();
        if (users.isEmpty()) {
            log.error("Ошибка при получении списка друзей");
            return Optional.empty();
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException("Юзер с id = " + id + " не найден");
        }
        if (users.containsKey(id) && !users.get(id).getFriends().isEmpty()) {
            for (Integer userFriendId : users.get(id).getFriends()) {
                result.add(users.get(userFriendId));
            }
            return Optional.of(result);
        }
        log.error("Ошибка при получении списка юзеров");
        return Optional.empty();
    }
}