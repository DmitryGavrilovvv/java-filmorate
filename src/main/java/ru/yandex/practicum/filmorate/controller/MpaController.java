package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.request.NewMpaRequest;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
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
        return ms.getAllRatings();
    }

    @PostMapping
    public MpaDto addMpa(@RequestBody NewMpaRequest request) {
        return ms.create(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{mpaId}")
    public void removeMpa(@PathVariable Integer mpaId) {
        ms.delete(mpaId);
    }

    @GetMapping("/{mpaId}")
    public MpaDto getMpaById(@PathVariable Integer mpaId) {
        return ms.getRatingById(mpaId);
    }
}
