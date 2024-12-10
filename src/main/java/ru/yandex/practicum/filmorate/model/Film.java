package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film implements Comparable<Film> {
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Integer> likes;
    @JsonIgnore
    private int rate = 0;

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public void addLike(Integer id) {
        likes.add(id);
        rate = likes.size();
    }

    public void removeLike(Integer id) {
        likes.remove(id);
        rate = likes.size();
    }

    public int compareTo(@NonNull Film obj) {
        return obj.getRate() - this.rate;
    }

}
