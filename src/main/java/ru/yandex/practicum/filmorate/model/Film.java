package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
@NoArgsConstructor
public class Film implements Comparable<Film> {
    private int id;
    @NotNull(message = "Название фильма не может быть пустым")
    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;
    @NotNull(message = "Описание фильма не может быть пустым")
    @NotEmpty(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание фильма не может быть больше 200 символов")
    private String description;
    @NotNull(message = "Дата релиза не может быть null")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    private Mpa mpa;
    @JsonIgnore
    private Set<Integer> likes;
    @JsonIgnore
    private int rateLikes = 0;
    @JsonIgnore
    private List<Genre> genres;
    @JsonIgnore
    private int rateGenres = 0;

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        likes = new HashSet<>();
        genres = new ArrayList<>();
    }

    public void addLike(Integer id) {
        likes.add(id);
        rateLikes = likes.size();
    }

    public void removeLike(Integer id) {
        likes.remove(id);
        rateLikes = likes.size();
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
        rateGenres = genres.size();
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        rateGenres = genres.size();
    }

    public int compareTo(@NonNull Film obj) {
        return obj.getRateLikes() - this.rateLikes;
    }
}
