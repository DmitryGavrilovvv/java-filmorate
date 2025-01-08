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
        return gs.getAllGenres();
    }

    @PostMapping
    public GenreDto addGenre(@RequestBody NewGenreRequest request) {
        return gs.create(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{genreId}")
    public void removeGenre(@PathVariable Integer genreId) {
        gs.delete(genreId);
    }

    @GetMapping("/{genreId}")
    public GenreDto getGenreById(@PathVariable Integer genreId) {
        return gs.getGenreById(genreId);
    }
}
