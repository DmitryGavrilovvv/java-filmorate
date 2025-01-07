package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.request.NewMpaRequest;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Controller
@RequestMapping("/mpa")
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService ms;

    @Autowired
    public MpaController(MpaService mpaService) {
        ms = mpaService;
    }

    @GetMapping
    public List<MpaDto> getAllMpa() {
        log.info("Пришел запрос Get /mpa");
        List<MpaDto> resMpa = ms.getAllRatings();
        log.info("Отправлен ответ Get /mpa : {}", resMpa);
        return resMpa;
    }

    @PostMapping
    public MpaDto addMpa(@RequestBody NewMpaRequest request) {
        log.info("пришел Post запрос /mpa с жанром: {}", request);
        MpaDto mpaDto = ms.create(request);
        log.info("Отправлен ответ Post /mpa с жанром: {}", mpaDto);
        return mpaDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{mpaId}")
    public void removeMpa(@PathVariable Integer mpaId) {
        log.info("пришел Delete запрос /mpa/{genreId} с id рейтинга {}", mpaId);
        ms.delete(mpaId);
        log.info("отправлен ответ Delete /mpa/{genreId} с id рейтинга {}", mpaId);
    }

    @GetMapping("/{mpaId}")
    public MpaDto getMpaById(@PathVariable Integer mpaId) {
        log.info("Пришел запрос Get /mpa/{mpaId}");
        MpaDto mpaDto = ms.getRatingById(mpaId);
        log.info("Отправлен ответ Get /mpa/{mpaId}");
        return mpaDto;
    }
}
