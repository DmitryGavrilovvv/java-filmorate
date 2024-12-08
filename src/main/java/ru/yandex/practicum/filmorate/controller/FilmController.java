package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage inMemoryFilmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Пришел запрос Get /films");
        Collection<Film> resFilms = inMemoryFilmStorage.findAll();
        log.info("Отправлен ответ Get /films : {}", resFilms);
        return resFilms;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("пришел Post запрос /films с фильмом: {}", film);
        inMemoryFilmStorage.create(film);
        log.info("Отправлен ответ Post /films с фильмом: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("пришел Put запрос /films с фильмом: {}", film);
        inMemoryFilmStorage.update(film);
        log.info("Отправлен ответ Put /films с фильмом: {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        log.info("пришел Put запрос /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
        filmService.addLike(userId, filmId);
        log.info("отправлен ответ Put /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        log.info("пришел Delete запрос /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
        filmService.removeLike(userId, filmId);
        log.info("отправлен ответ Delete /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        log.info("Пришел запрос Get /films/popular");
        List<Film> films = filmService.getBestFilms(Objects.requireNonNullElse(count, 10));
        log.info("Отправлен ответ Get /films/popular");
        return films;
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        log.info("Пришел запрос Get /films/{filmId}");
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        log.info("Пришел запрос Get /films/{filmId}");
        if (film0.isPresent()) {
            return film0.get();
        } else throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
    }


}
