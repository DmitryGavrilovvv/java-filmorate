package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Genre {
    private Integer id;
    private String name;

    @NotBlank(message = "Название жанра не может быть пустым")
    public Genre(String name) {
        this.name = name;
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
