package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.like.LikeStorage;

import java.util.List;

@Repository
public class LikeRepository extends BaseRepository<Film> implements LikeStorage {
    private static final String FIND_BEST_QUERY = "SELECT f.*,r.name as mpa_name, COUNT(l.user_id) as like_count " +
            "FROM films as f LEFT JOIN likes as l ON l.film_id = f.film_id " +
            "LEFT JOIN rating as r ON r.rating_id = f.rating_id" +
            "GROUP BY f.film_id ORDER NY like_count DESC LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO likes(film_id,user_id) VALUES (?,?)";
    private static final String DELETE_QUERY = "DELETE FROM likes WHERE film_id =?, user_id = ?";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(int filmId, int userId) {
        insert(INSERT_QUERY, filmId, userId);
    }

    @Override
    public boolean removeLike(int filmId, int userId) {
        return delete(DELETE_QUERY, filmId, userId);
    }

    @Override
    public List<Film> findBestFilms(int count) {
        return findMany(FIND_BEST_QUERY, count);
    }
}
