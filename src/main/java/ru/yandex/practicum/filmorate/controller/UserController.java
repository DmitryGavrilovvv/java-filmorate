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
        log.debug("Пришел запрос Get /users");
        Collection<UserDto> resUsers = userService.findAll();
        log.debug("Отправлен ответ Get /users : {}", resUsers);
        return resUsers;
    }

    @PostMapping
    public UserDto addUser(@RequestBody NewUserRequest newUser) {
        log.debug("пришел Post запрос /users с пользователем: {}", newUser);
        UserDto user = userService.create(newUser);
        log.debug("Отправлен ответ Post /users с пользователем: {}", user);
        return user;
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UpdateUserRequest newUser) {
        log.debug("пришел Put запрос /users с пользователем: {}", newUser);
        UserDto user = userService.update(newUser.getId(), newUser);
        log.debug("Отправлен ответ Put /users с пользователем: {}", user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        log.debug("пришел Delete запрос /users/{id} с id пользователя: {}", id);
        userService.removeUser(id);
        log.debug("отправлен Delete ответ /users/{id} с id пользователя: {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("пришел Put запрос /users с id пользователей: {},{}", id, friendId);
        userService.addFriend(id, friendId);
        log.debug("Отправлен ответ Put /users с id пользователей: {},{}", id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("пришел Delete запрос /users/{id}/friends/{friendId} с id пользователей: {},{}", id, friendId);
        UserDto userDto = userService.removeFriend(id, friendId);
        log.debug("Отправлен Delete Put /users/{id}/friends/{friendId} с id пользователей: {},{}", id, friendId);
        return userDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Integer id) {
        log.debug("Пришел запрос Get /users/{id}/friends с id пользователя {}", id);
        Optional<List<UserDto>> friendsList = userService.getFriends(id);
        log.debug("Отправлен ответ Get /users/{id}/friends с id пользователя {}", id);
        return friendsList.orElse(Collections.emptyList());
    }

    @GetMapping("/{userId}/friends/{friendId}")
    public UserDto getFriendById(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.debug("пришел Put запрос /users/{userId}/friends/{friendId} с id пользователей: {},{}", userId, friendId);
        UserDto userDto = userService.getFriendById(userId, friendId);
        log.debug("Отправлен ответ Put /users/{userId}/friends/{friendId} с id пользователей: {},{}", userId, friendId);
        return userDto;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.debug("пришел Put запрос /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        Collection<UserDto> friends = userService.getMutualFriends(id, otherId);
        log.debug("Отправлен ответ Put /users/{id}/friends/common/{otherId} с id пользователей: {},{}", id, otherId);
        return friends;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Integer userId) {
        log.debug("Пришел запрос Get /users/{userId} с id пользователя {}", userId);
        UserDto user = userService.getUserById(userId);
        log.debug("Отправлен ответ Get /users/{userId} с id пользователя {}", userId);
        return user;
    }
}
