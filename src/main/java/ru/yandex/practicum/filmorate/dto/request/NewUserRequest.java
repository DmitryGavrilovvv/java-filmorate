package ru.yandex.practicum.filmorate.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
