package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    public final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Отправлен ответ Get /users : {}", users.values());
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("пришел Post запрос /users с пользователем: {}", user.toString());
        validateUser(user);
        user.setId(++idGenerator);
        users.put(user.getId(), user);
        log.info("Отправлен ответ Post /users с пользователем: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("пришел Put запрос /users с пользователем: {}", user.toString());
        validateUser(user);
        User oldUser = users.get(user.getId());
        if (oldUser == null) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Отправлен ответ Put /users с пользователем: {}", user);
        return user;
    }

    private void validateUser(User user) {
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            log.error("Ошибка при добавлении пользователя: некорректная почта - {}", email);
            throw new ValidateException("Некорректная электронная почта.");
        }
        String login = user.getLogin();
        if (login == null || login.isEmpty() || login.isBlank() || login.contains(" ")) {
            log.error("Ошибка при добавлении пользователя: некорректный логин - {}", login);
            throw new ValidateException("Некорректный логин.");
        }
        String name = user.getName();
        if (name == null || name.isEmpty() || name.isBlank()) {
            log.info("Пользователь использует логин - {} вместо имени", user.getLogin());
            user.setName(user.getLogin());
        }
        LocalDate birthday = user.getBirthday();
        if (birthday == null || birthday.isAfter(LocalDate.now())) {
            log.error("Ошибка при добавлении пользователя: некоррктная дата рождения - {}", birthday);
            throw new ValidateException("Некорректная дата рождения.");
        }
    }
}
