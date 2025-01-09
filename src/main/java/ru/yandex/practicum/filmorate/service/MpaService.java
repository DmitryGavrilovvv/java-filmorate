package ru.yandex.practicum.filmorate.service;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.request.NewMpaRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.rating.RatingStorage;

import java.util.List;

@Service
public class MpaService {
    private static final Logger log = LoggerFactory.getLogger(MpaService.class);
    private final RatingStorage rs;

    @Autowired
    public MpaService(MpaRepository mpaRepository) {
        rs = mpaRepository;
    }

    public MpaDto getRatingById(Integer id) {
        return rs.getRatingById(id)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рэйтинг не найден"));
    }

    public MpaDto create(NewMpaRequest request) {
        Mpa mpa = MpaMapper.mapToMpa(request);
        if (mpa.getName().isEmpty() || StringUtils.isBlank(mpa.getName())) {
            log.error("Ошибка при валидации рейтинга: название не может быть пустым");
            throw new ValidateException("Название рейтинга не может быть пустым");
        }
        mpa = rs.create(mpa);
        return MpaMapper.mapToMpaDto(mpa);
    }

    public boolean delete(Integer id) {
        return rs.delete(id);
    }

    public List<MpaDto> getAllRatings() {
        return rs.getAllRatings().stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }
}
