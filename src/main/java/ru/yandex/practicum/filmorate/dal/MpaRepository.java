package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.rating.RatingStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> implements RatingStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM MpaRatings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM MpaRatings WHERE mpa_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO MpaRatings(name) VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM MpaRatings WHERE mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> getRatingById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Mpa create(Mpa mpa) {
        int id = insert(INSERT_QUERY, mpa.getName());
        mpa.setId(id);
        return mpa;
    }

    @Override
    public boolean delete(Integer id) {
        return delete(DELETE_QUERY, id);
    }

    @Override
    public List<Mpa> getAllRatings() {
        return findMany(FIND_ALL_QUERY);
    }
}
