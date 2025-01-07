package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Genre {
    private int id;
    @NonNull
    private String name;

    public Genre(@NonNull String name) {
        this.name = name;
    }
}
