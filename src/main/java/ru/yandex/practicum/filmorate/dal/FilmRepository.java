package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration, r.name as mpa_name FROM films LEFT JOIN rating as r ON r.rating_id = f.rating_id";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration, r.name as mpa_name FROM films as f " +
                    "LEFT JOIN rating as r ON r.rating_id = f.rating_id WHERE f.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name,description,release_date,duration,rating_id) " +
            "VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        int mpaId = film.getMpa().getId();
        int id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate().toString()),
                film.getDuration(),
                mpaId);
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        int mpaId = newFilm.getMpa().getId();
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate().toString()),
                newFilm.getDuration(),
                mpaId,
                newFilm.getId());
        return newFilm;
    }

    @Override
    public boolean delete(Integer id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
