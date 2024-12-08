package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film implements Comparable<Film> {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public void addLike(Integer id) {
        if (id == null) {
            throw new NullPointerException("id не должен быть null");
        }
        likes.add(id);
    }

    public void removeLike(Integer id) {
        if (id == null) {
            throw new NullPointerException("id не должен быть null");
        }
        likes.remove(id);
    }

    public int compareTo(Film obj) {
        return obj.getLikes().size() - this.getLikes().size();
    }

}
