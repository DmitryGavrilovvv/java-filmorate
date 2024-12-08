package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, UserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Пришел запрос Get /users");
        Collection<User> resUsers = inMemoryUserStorage.findAll();
        log.info("Отправлен ответ Get /users : {}", resUsers);
        return resUsers;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("пришел Post запрос /users с пользователем: {}", user);
        inMemoryUserStorage.create(user);
        log.info("Отправлен ответ Post /users с пользователем: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("пришел Put запрос /users с пользователем: {}", user);
        inMemoryUserStorage.update(user);
        log.info("Отправлен ответ Put /users с пользователем: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("пришел Put запрос /users с id пользователей: {},{}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Отправлен ответ Put /users с id пользователей: {},{}", id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Пришел запрос Get /users/{id}/friends с id пользователя {}", id);
        Optional<List<User>> friendsList = inMemoryUserStorage.getFriends(id);
        log.info("Отправлен ответ Get /users/{id}/friends с id пользователя {}", id);
        return friendsList.orElse(Collections.emptyList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("пришел Put запрос /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        Collection<User> friends = userService.getMutualFriends(id, otherId);
        log.info("Отправлен ответ Put /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        return friends;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        log.info("Пришел запрос Get /users/{userId} с id пользователя {}", userId);
        Optional<User> user = inMemoryUserStorage.getUserById(userId);
        log.info("Отправлен ответ Get /users/{userId} с id пользователя {}", userId);
        if (user.isPresent()) {
            return user.get();
        } else throw new NotFoundException("Юзер с " + userId + " отсутствует.");
    }


}
