package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration,r.mpa_id, r.name as mpa_name FROM films as f LEFT JOIN mparatings as r ON r.mpa_id = f.mpa_id";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.film_id, f.name, f.description, f.release_date, " +
                    "f.duration,r.mpa_id, r.name as mpa_name FROM films as f " +
                    "LEFT JOIN mparatings as r ON r.mpa_id = f.mpa_id WHERE f.film_id = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.genre_id,g.name FROM Genres as g " +
            "LEFT JOIN FilmsGenres as fg on g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name,description,release_date,duration,mpa_id) " +
            "VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        for (Film film : films) {
            getFilmGenres(film);
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        Integer mpaId = null;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        int id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate().toString()),
                film.getDuration(),
                mpaId);
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            validateGenresExist(film.getGenres());

            String insertGenresSql = "INSERT INTO FilmsGenres (film_id, genre_id) VALUES (?, ?)";
            Set<Integer> uniqueGenreIds = new HashSet<>();
            List<Object[]> batchArgs = new ArrayList<>();

            for (Genre genre : film.getGenres()) {
                if (uniqueGenreIds.add(genre.getId())) {
                    batchArgs.add(new Object[]{film.getId(), genre.getId()});
                }
            }

            jdbc.batchUpdate(insertGenresSql, batchArgs);
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (!filmExists(newFilm.getId())) {
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не найден");
        }
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
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, id);
        return film.map(this::getFilmGenres);
    }

    private Film getFilmGenres(Film film) {
        List<Genre> genres = jdbc.query(FIND_FILM_GENRES_QUERY, new GenreRowMapper(), film.getId());
        film.setGenres(genres);
        return film;

    }

    private void validateGenresExist(Collection<Genre> genres) {
        Set<Integer> genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        String sqlQuery = String.format("SELECT genre_id FROM Genres WHERE genre_id IN (%s)",
                genreIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));

        List<Long> existingIds = jdbc.query(sqlQuery, (rs, rowNum) -> rs.getLong("genre_id"));

        if (existingIds.size() != genreIds.size()) {
            genreIds.removeAll(existingIds);
            throw new ValidateException("Некоторые жанры не существуют: " + genreIds);
        }

    }

    private boolean filmExists(Integer filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM Films WHERE film_id = ?";
        Integer count = jdbc.queryForObject(sqlQuery, Integer.class, filmId);
        return count != null && count > 0;
    }
}
