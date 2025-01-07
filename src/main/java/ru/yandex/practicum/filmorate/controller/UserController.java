package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.request.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.request.UpdateUserRequest;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Пришел запрос Get /users");
        Collection<UserDto> resUsers = userService.findAll();
        log.info("Отправлен ответ Get /users : {}", resUsers);
        return resUsers;
    }

    @PostMapping
    public UserDto addUser(@RequestBody NewUserRequest newUser) {
        log.info("пришел Post запрос /users с пользователем: {}", newUser);
        UserDto user = userService.create(newUser);
        log.info("Отправлен ответ Post /users с пользователем: {}", user);
        return user;
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UpdateUserRequest newUser) {
        log.info("пришел Put запрос /users с пользователем: {}", newUser);
        UserDto user = userService.update(newUser.getId(), newUser);
        log.info("Отправлен ответ Put /users с пользователем: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("пришел Put запрос /users с id пользователей: {},{}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Отправлен ответ Put /users с id пользователей: {},{}", id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("пришел Delete запрос /users с id пользователей: {},{}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Отправлен Delete Put /users с id пользователей: {},{}", id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Integer id) {
        log.info("Пришел запрос Get /users/{id}/friends с id пользователя {}", id);
        Optional<List<UserDto>> friendsList = userService.getFriends(id);
        log.info("Отправлен ответ Get /users/{id}/friends с id пользователя {}", id);
        return friendsList.orElse(Collections.emptyList());
    }

    @GetMapping("/{userId}/friends/{friendId}")
    public UserDto getFriendById(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("пришел Put запрос /users/{userId}/friends/{friendId} с id пользователей: {},{}", userId, friendId);
        UserDto userDto = userService.getFriendById(userId, friendId);
        log.info("Отправлен ответ Put /users/{userId}/friends/{friendId} с id пользователей: {},{}", userId, friendId);
        return userDto;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("пришел Put запрос /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        Collection<UserDto> friends = userService.getMutualFriends(id, otherId);
        log.info("Отправлен ответ Put /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        return friends;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {
        log.info("Пришел запрос Get /users/{userId} с id пользователя {}", userId);
        UserDto user = userService.getUserById(userId);
        log.info("Отправлен ответ Get /users/{userId} с id пользователя {}", userId);
        return user;
    }


}
