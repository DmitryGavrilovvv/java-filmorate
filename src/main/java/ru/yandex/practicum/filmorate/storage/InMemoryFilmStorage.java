package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        film.setId(++idGenerator);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
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
}
