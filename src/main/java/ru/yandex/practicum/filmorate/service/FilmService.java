package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addLike(Integer userId, Integer filmId) {
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при добавлении лайка: пользователь с id {}  не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Film> filmOptional = inMemoryFilmStorage.getFilmById(filmId);
        if (filmOptional.isPresent()) {
            filmOptional.get().addLike(userId);
            log.trace("Фильму с id {} поставлен лайк пользователем с id {}", filmId, userId);
        } else {
            log.error("Ошибка при добавлении лайка: фильм с id {}  не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    public void removeLike(Integer userId, Integer filmId) {
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при удалении лайка: пользователь с id {}  не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Film> filmOptional = inMemoryFilmStorage.getFilmById(filmId);
        if (filmOptional.isPresent()) {
            filmOptional.get().removeLike(userId);
            log.trace("Фильму с id {} удалён лайк пользователем с id {}", filmId, userId);
        } else {
            log.error("Ошибка при удалении лайка: фильм с id {}  не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    public List<Film> getBestFilms(Integer count) {
        List<Film> allFilms = inMemoryFilmStorage.findAll();
        return allFilms.stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }
}
