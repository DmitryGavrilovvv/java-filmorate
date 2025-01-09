package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genres(name) VALUES(?)";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Genre create(Genre genre) {
        int id = insert(INSERT_QUERY, genre.getName());
        genre.setId(id);
        return genre;
    }

    @Override
    public boolean delete(Integer id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }
}
