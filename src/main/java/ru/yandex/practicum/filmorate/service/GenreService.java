package ru.yandex.practicum.filmorate.service;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.request.NewGenreRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private static final Logger log = LoggerFactory.getLogger(GenreService.class);
    private final GenreStorage gs;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        gs = genreRepository;
    }

    public GenreDto getGenreById(Integer id) {
        return gs.getGenreById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Не найден жанр с Id:" + id));
    }

    public GenreDto create(NewGenreRequest request) {
        Genre genre = GenreMapper.mapToGenre(request);
        if (genre.getName().isEmpty() || StringUtils.isBlank(genre.getName())) {
            log.warn("Ошибка при валидации жанра: название не может быть пустым");
            throw new ValidateException("Название жанра не может быть пустым");
        }
        genre = gs.create(genre);
        return GenreMapper.mapToGenreDto(genre);
    }

    public void delete(Integer id) {
        gs.delete(id);
    }

    public List<GenreDto> getAllGenres() {
        return gs.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }
}
