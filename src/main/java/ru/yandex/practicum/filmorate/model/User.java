package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;


@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Integer> friends;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        friends = new HashSet<>();
    }

    public void addFriend(Integer id) {
        if (id == null) {
            throw new NullPointerException("id не должен быть null");
        }
        friends.add(id);
    }

    public void removeFriend(Integer id) {
        if (id == null) {
            throw new NullPointerException("id не должен быть null");
        }
        friends.remove(id);
    }

    public Optional<Integer> getFriend(Integer id) {
        if (id == null) {
            throw new NullPointerException("id не должен быть null");
        }

        if (!friends.contains(id)) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    public List<Integer> getAllFriends() {
        return friends.stream().toList();
    }
}
