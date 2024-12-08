package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        film.setId(++idGenerator);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        validateFilm(newFilm);
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            log.error("Фильм с id {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм не найден");
        }
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        }
        log.error("Ошибка при получении списка фильмов");
        return Optional.empty();
    }

    private void validateFilm(Film film) {
        String name = film.getName();
        if (name == null || name.isEmpty() || name.isBlank()) {
            log.error("Ошибка при добавлении фильма: введено пустое название");
            throw new ValidateException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка при добавлении фильма: превышена максимальная длина описания");
            throw new ValidateException("Максимальная длина строки - 200 символов.");
        }
        if (film.getDuration() < 1) {
            log.error("Ошибка при добавлении фильма: введена некорректная продолжительность - {}", film.getDuration());
            throw new ValidateException("Продолжительность не может быть отрицательной.");
        }
        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate == null) {
            log.error("Ошибка при добавлении фильма: введена пустая дата релиза");
            throw new ValidateException("Дата релиза не может быть пустой");
        } else if (releaseDate.isBefore(MOVIE_BIRTHDAY)) {
            log.error("Ошибка при добавлении фильма: введена дата релиза раньше 28 декабря 1985 года - {}", releaseDate);
            throw new ValidateException("Релиз не может быть раньше 28 декабря 1985 года.");
        }
    }
}
