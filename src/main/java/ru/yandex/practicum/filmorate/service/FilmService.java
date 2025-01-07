package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.request.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.request.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class FilmService {
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage fs;
    private final LikeStorage ls;
    private final UserStorage us;

    @Autowired
    public FilmService(FilmRepository filmRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.fs = filmRepository;
        this.ls = likeRepository;
        this.us = userRepository;
    }

    public List<FilmDto> findAll() {
        return fs.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto create(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        validateFilm(film);
        film = fs.create(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(Integer id, UpdateFilmRequest request) {
        Film film = fs.getFilmById(id)
                .map(film1 -> FilmMapper.updateFilmFields(film1, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        validateFilm(film);
        film = fs.update(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto getFilmById(Integer id) {
        return fs.getFilmById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с id" + id));
    }

    public void addLike(Integer userId, Integer filmId) {
        Optional<User> userOptional = us.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при добавлении лайка: пользователь с id {}  не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Film film = fs.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));
        ls.addLike(filmId, userId);
        log.trace("Фильму с id {} поставлен лайк пользователем с id {}", filmId, userId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        Optional<User> userOptional = us.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при удалении лайка: пользователь с id {}  не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Film> filmOptional = fs.getFilmById(filmId);
        if (filmOptional.isPresent()) {
            ls.removeLike(filmId, userId);
            log.trace("Фильму с id {} удалён лайк пользователем с id {}", filmId, userId);
        } else {
            log.error("Ошибка при удалении лайка: фильм с id {}  не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    public List<FilmDto> getBestFilms(Integer count) {
        List<Film> allFilms = fs.findAll();
        return allFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
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
