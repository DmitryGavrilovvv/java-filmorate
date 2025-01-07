package ru.yandex.practicum.filmorate.storage.film.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {
    Optional<Mpa> getRatingById(Integer id);

    Mpa create(Mpa mpa);

    boolean delete(Integer id);

    List<Mpa> getAllRatings();
}
