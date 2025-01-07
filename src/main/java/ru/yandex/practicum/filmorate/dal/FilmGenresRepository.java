package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.genre.FilmGenresStorage;

import java.util.List;

@Repository
public class FilmGenresRepository extends BaseRepository<Integer> implements FilmGenresStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT g.name from genre as g RIGHT JOIN filmsgenres as fg " +
            "ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO filmsgenres(film_id,genre_id) VALUES(?,?)";
    private static final String DELETE_QUERY = "DELETE FROM filmsgenres where film_id =?, genre_id = ?";

    public FilmGenresRepository(JdbcTemplate jdbc, RowMapper<Integer> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Integer create(int filmId, int genreId) {
        return jdbc.update(INSERT_QUERY, filmId, genreId);
    }

    @Override
    public boolean delete(int filmId, int genreId) {
        return delete(DELETE_QUERY, filmId, genreId);
    }

    @Override
    public List<Integer> getFilmGenres(int filmId) {
        return findMany(FIND_BY_ID_QUERY, filmId);
    }
}
