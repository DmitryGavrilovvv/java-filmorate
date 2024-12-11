package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Data
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends;
    @JsonIgnore
    private int rate = 0;

    public User(@NonNull String email, @NonNull String login, String name, @NonNull LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        friends = new HashSet<>();
    }

    public void addFriend(Integer id) {
        friends.add(id);
        rate = friends.size();
    }

    public void removeFriend(Integer id) {
        friends.remove(id);
        rate = friends.size();
    }

    public Optional<Integer> getFriend(Integer id) {
        if (!friends.contains(id)) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    public List<Integer> getAllFriends() {
        return friends.stream().toList();
    }
}
