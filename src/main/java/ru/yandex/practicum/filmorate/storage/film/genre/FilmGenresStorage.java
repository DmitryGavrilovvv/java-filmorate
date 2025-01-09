package ru.yandex.practicum.filmorate.storage.film.genre;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmGenresStorage {
    Integer create(int filmId, int genreId);

    boolean delete(int filmId, int genreId);

    List<Integer> getFilmGenres(int filmId);
}
