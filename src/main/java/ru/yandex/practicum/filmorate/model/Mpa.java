package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Mpa {
    private int id;
    @NonNull
    private String name;

    public Mpa(@NonNull String name) {
        this.name = name;
    }
}
