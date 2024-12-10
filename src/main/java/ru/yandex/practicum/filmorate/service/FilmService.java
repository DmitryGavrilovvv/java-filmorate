package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return inMemoryFilmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        Optional<Film> film = inMemoryFilmStorage.getFilmById(id);
        if (film.isPresent()) {
            return film.get();
        } else throw new NotFoundException("Фильм с " + id + " отсутствует.");
    }

    public void addLike(Integer userId, Integer filmId) {
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при добавлении лайка: пользователь с id {}  не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Film film = inMemoryFilmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));
        film.addLike(userId);
        log.trace("Фильму с id {} поставлен лайк пользователем с id {}", filmId, userId);
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


    private void validateFilm(Film film) {
        String name = film.getName();
        validate(() -> name.isEmpty() || name.isBlank(), "Название не может быть пустым.");
        validate(() -> film.getDescription().length() > 200, "Максимальная длина строки - 200 символов.");
        validate(() -> film.getDuration() < 1, "Продолжительность не может быть отрицательной.");
        LocalDate releaseDate = film.getReleaseDate();
        validate(() -> releaseDate.isBefore(MOVIE_BIRTHDAY),
                "Релиз не может быть раньше 28 декабря 1985 года.");
    }

    private void validate(Supplier<Boolean> supplier, String massage) {
        if (supplier.get()) {
            log.error("Ошибка при валидации фильма: {}", massage);
            throw new ValidateException(massage);
        }
    }
}
