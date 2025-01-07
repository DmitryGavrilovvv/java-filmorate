package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenreById(Integer id);

    Genre create(Genre genre);

    boolean delete(Integer id);

    List<Genre> getAllGenres();
}
