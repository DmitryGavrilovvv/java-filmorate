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
        return filmService.findAll();
    }

    @PostMapping
    public FilmDto addFilm(@RequestBody NewFilmRequest newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody UpdateFilmRequest newFilm) {
        return filmService.update(newFilm.getId(), newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        filmService.removeLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getBestFilms(count);
    }

    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable Integer filmId) {
        return filmService.getFilmById(filmId);
    }


}
