package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.request.NewGenreRequest;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreService gs;

    @Autowired
    public GenreController(GenreService genreService) {
        gs = genreService;
    }

    @GetMapping
    public List<GenreDto> getGenres() {
        log.info("Пришел запрос Get /genres");
        List<GenreDto> resGenres = gs.getAllGenres();
        log.info("Отправлен ответ Get /genres : {}", resGenres);
        return resGenres;
    }

    @PostMapping
    public GenreDto addGenre(@RequestBody NewGenreRequest request) {
        log.info("пришел Post запрос /genres с жанром: {}", request);
        GenreDto genreDto = gs.create(request);
        log.info("Отправлен ответ Post /genres с жанром: {}", genreDto);
        return genreDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{genreId}")
    public void removeGenre(@PathVariable Integer genreId) {
        log.info("пришел Delete запрос /genres/{genreId} с id жанра {}", genreId);
        gs.delete(genreId);
        log.info("отправлен ответ Delete /genres/{genreId} с id жанра {}", genreId);
    }

    @GetMapping("/{genreId}")
    public GenreDto getGenreById(@PathVariable Integer genreId) {
        log.info("Пришел запрос Get /genres/{genreId}");
        GenreDto genreDto = gs.getGenreById(genreId);
        log.info("Отправлен ответ Get /genres/{genreId}");
        return genreDto;
    }
}
