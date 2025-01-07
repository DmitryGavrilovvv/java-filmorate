package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.request.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.request.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friendship.FriendshipStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage us;
    private final FriendshipStorage fss;

    @Autowired
    public UserService(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.fss = friendshipRepository;
        this.us = userRepository;
    }

    public List<UserDto> findAll() {
        return us.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto create(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);
        validateUser(user);
        user = us.create(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(Integer id, UpdateUserRequest request) {
        User updateUser = us.getUserById(id)
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        validateUser(updateUser);
        us.update(updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    public UserDto getUserById(Integer id) {
        return us.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID:" + id));
    }

    public Optional<List<UserDto>> getFriends(Integer id) {
        List<UserDto> result = fss.getFriends(id)
                .stream()
                .filter(Objects::nonNull)
                .map(UserMapper::mapToUserDto)
                .toList();
        return Optional.of(result);
    }

    public UserDto getFriendById(Integer userId, Integer friendId) {
        return fss.getFriendById(userId, friendId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Друг не найден"));
    }

    public void addFriend(Integer userId1, Integer userId2) {
        if (Objects.equals(userId1, userId2)) {
            log.error("Нельзя добавить в друзья самого себя");
            throw new ValidateException("Нельзя добавить в друзья самого себя");
        }
        Optional<User> user1 = us.getUserById(userId1);
        Optional<User> user2 = us.getUserById(userId2);
        if (user1.isEmpty() || user2.isEmpty()) {
            log.error("Ошибка при добавлении друзей: один из пользователей не найден. {}, {}", userId2, userId1);
            throw new NotFoundException("Один из пользователей не найден");
        }
        fss.addFriend(userId1, userId2);
        log.info("Пользователи добавлены в друзья");
    }

    public void removeFriend(Integer userId1, Integer userId2) {
        Optional<User> userOptional1 = us.getUserById(userId1);
        Optional<User> userOptional2 = us.getUserById(userId2);
        if (userOptional1.isEmpty() || userOptional2.isEmpty()) {
            log.error("Ошибка при удалении друзей: один из пользователей не найден. {}, {}", userId2, userId1);
            throw new NotFoundException("Один из пользователей не найден");
        }
        fss.deleteFriend(userId1, userId2);
        log.info("Пользователи удалены из друзей");
    }

    public Collection<UserDto> getMutualFriends(Integer userId1, Integer userId2) {
        Optional<User> userOptional1 = us.getUserById(userId1);
        Optional<User> userOptional2 = us.getUserById(userId2);
        if (userOptional1.isEmpty() || userOptional2.isEmpty()) {
            log.error("Ошибка при получении общих друзей");
            throw new NotFoundException("Один из пользователей не найден");
        }
        List<User> result = fss.getMutualFriends(userId1, userId2);
        if (result.isEmpty()) {
            log.error("Нет общих друзей");
            throw new NotFoundException("Общие друзья не найдены");
        }
        log.info("Возвращён список общих друзей пользователей: {}, {}", userId2, userId1);
        return result.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
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
        if (name == null || name.isEmpty() || name.isBlank()) {
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
