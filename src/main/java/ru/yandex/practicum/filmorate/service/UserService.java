package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        validateUser(user);
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        validateUser(user);
        return inMemoryUserStorage.update(user);
    }

    public User getUserById(Integer id) {
        Optional<User> user = inMemoryUserStorage.getUserById(id);
        if (user.isPresent()) {
            return user.get();
        } else throw new NotFoundException("Юзер с " + id + " отсутствует.");
    }

    public Optional<List<User>> getFriends(Integer id) {
        return inMemoryUserStorage.getFriends(id);
    }

    public void addFriend(Integer userId1, Integer userId2) {
        Optional<User> user1 = inMemoryUserStorage.getUserById(userId1);
        Optional<User> user2 = inMemoryUserStorage.getUserById(userId2);
        if (user1.isEmpty() || user2.isEmpty()) {
            log.error("Ошибка при добавлении друзей: один из пользователей не найден. {}, {}", userId2, userId1);
            throw new NotFoundException("Один из пользователей не найден");
        }
        user1.get().addFriend(userId2);
        user2.get().addFriend(userId1);
        log.info("Пользователи добавлены в друзья");
    }

    public void removeFriend(Integer userId1, Integer userId2) {
        Optional<User> userOptional1 = inMemoryUserStorage.getUserById(userId1);
        Optional<User> userOptional2 = inMemoryUserStorage.getUserById(userId2);
        if (userOptional1.isEmpty() || userOptional2.isEmpty()) {
            log.error("Ошибка при удалении друзей: один из пользователей не найден. {}, {}", userId2, userId1);
            throw new NotFoundException("Один из пользователей не найден");
        }
        userOptional1.get().removeFriend(userId2);
        userOptional2.get().removeFriend(userId1);
        log.info("Пользователи удалены из друзей");
    }

    public Collection<User> getMutualFriends(Integer userId1, Integer userId2) {
        Optional<User> userOptional1 = inMemoryUserStorage.getUserById(userId1);
        Optional<User> userOptional2 = inMemoryUserStorage.getUserById(userId2);
        List<User> result = new ArrayList<>();
        if (userOptional1.isEmpty() || userOptional2.isEmpty()) {
            log.error("Ошибка при получении общих друзей");
            throw new NotFoundException("Один из пользователей не найден");
        }
        List<Integer> ids = userOptional1.get().getAllFriends();
        User user2 = userOptional2.get();
        for (Integer id : ids) {
            if (user2.getFriend(id).isPresent()) {
                inMemoryUserStorage.getUserById(id).ifPresent(result::add);
            }
        }
        if (result.isEmpty()) {
            log.error("Нет общих друзей");
            throw new NotFoundException("Общие друзья не найдены");
        }
        log.info("Возвращён список общих друзей пользователей: {}, {}", userId2, userId1);
        return result;
    }

    private void validateUser(User user) {
        String email = user.getEmail();
        if (email.isBlank() || !email.contains("@")) {
            log.error("Ошибка при добавлении пользователя: некорректная почта - {}", email);
            throw new ValidateException("Некорректная электронная почта.");
        }
        String login = user.getLogin();
        if (login.isEmpty() || login.isBlank() || login.contains(" ")) {
            log.error("Ошибка при добавлении пользователя: некорректный логин - {}", login);
            throw new ValidateException("Некорректный логин.");
        }
        String name = user.getName();
        if (name.isEmpty() || name.isBlank()) {
            log.info("Пользователь использует логин - {} вместо имени", user.getLogin());
            user.setName(user.getLogin());
        }
        LocalDate birthday = user.getBirthday();
        if (birthday.isAfter(LocalDate.now())) {
            log.error("Ошибка при добавлении пользователя: некоррктная дата рождения - {}", birthday);
            throw new ValidateException("Некорректная дата рождения.");
        }
    }

}
