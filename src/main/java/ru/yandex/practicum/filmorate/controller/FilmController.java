package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.request.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.request.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmDto> getFilms() {
        log.debug("Пришел запрос Get /films");
        Collection<FilmDto> resFilms = filmService.findAll();
        log.debug("Отправлен ответ Get /films : {}", resFilms);
        return resFilms;
    }

    @PostMapping
    public FilmDto addFilm(@RequestBody NewFilmRequest newFilm) {
        log.debug("пришел Post запрос /films с фильмом: {}", newFilm);
        FilmDto film = filmService.create(newFilm);
        log.debug("Отправлен ответ Post /films с фильмом: {}", film);
        return film;
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody UpdateFilmRequest newFilm) {
        log.debug("пришел Put запрос /films с фильмом: {}", newFilm);
        FilmDto film = filmService.update(newFilm.getId(), newFilm);
        log.debug("Отправлен ответ Put /films с фильмом: {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        log.debug("пришел Put запрос /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
        filmService.addLike(userId, filmId);
        log.debug("отправлен ответ Put /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        log.debug("пришел Delete запрос /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
        filmService.removeLike(userId, filmId);
        log.debug("отправлен ответ Delete /films/{id}/like/{userId} с id пользователя {}, и id фильма {}", userId, filmId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.debug("Пришел запрос Get /films/popular");
        List<FilmDto> films = filmService.getBestFilms(count);
        log.debug("Отправлен ответ Get /films/popular");
        return films;
    }

    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable Integer filmId) {
        log.debug("Пришел запрос Get /films/{filmId}");
        FilmDto film = filmService.getFilmById(filmId);
        log.debug("Отправлен ответ Get /films/{filmId}");
        return film;
    }


}
