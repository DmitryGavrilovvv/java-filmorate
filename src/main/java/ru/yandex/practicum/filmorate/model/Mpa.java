package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {
    private Integer id;
    private String name;

    @NotBlank(message = "Название MPA не может быть пустым")
    public Mpa(String name) {
        this.name = name;
    }

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
