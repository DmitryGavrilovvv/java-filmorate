package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserStorage storage = new InMemoryUserStorage();
    UserService service = new UserService(storage);
    UserController us = new UserController(service);

    @Test
    void checkEmail() {
        User user1 = new User("someWords", "Itsme", "Name", LocalDate.now().minusYears(20));

        assertThrows(ValidateException.class, () -> us.addUser(user1));
    }

    @Test
    void emptyEmail() {
        User user1 = new User(" ", "Itsme", "Name", LocalDate.now().minusYears(20));

        assertThrows(ValidateException.class, () -> us.addUser(user1));
    }

    @Test
    void emptyName() {
        User user1 = new User("myemail@practicum.ru", "Itsme", " ", LocalDate.now().minusYears(20));
        us.addUser(user1);
        assertEquals(user1.getLogin(), storage.getUserById(user1.getId()).get().getName());
    }

    @Test
    void emptyLogin() {
        User user1 = new User("myemail@practicum.ru", "", "Name", LocalDate.now().minusYears(20));

        assertThrows(ValidateException.class, () ->
                us.addUser(user1)
        );
    }

    @Test
    void birthdayInFuture() {
        User user1 = new User("myemail@practicum.ru", "Itsme", "Name", LocalDate.now().plusMonths(20));

        assertThrows(ValidateException.class, () -> us.addUser(user1));
    }

    @Test
    void spaceInLogin() {
        User user1 = new User("myemail@practicum.ru", "Its me", "Name", LocalDate.now());

        assertThrows(ValidateException.class, () -> us.addUser(user1));
    }

    @Test
    void testChangingUserWithEmptyName() {
        User user = new User("myemail@practicum.ru", "Itsme1", null, LocalDate.now());
        us.addUser(user);

        assertEquals(user.getName(), user.getLogin());
    }
}
