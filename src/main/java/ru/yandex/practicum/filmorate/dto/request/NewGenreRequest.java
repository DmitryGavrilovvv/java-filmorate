package ru.yandex.practicum.filmorate.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewGenreRequest {
    private Integer id;
    @NotBlank(message = "Название MPA не может быть пустым")
    private String name;
}
