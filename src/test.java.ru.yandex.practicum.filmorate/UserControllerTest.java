import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

class UserControllerTest {
    @Autowired
    //private final UserController controller = new UserController();
    private UserController controller;
    private final User user = User.builder()
            .id(1)
            .email("email@yandex.ru")
            .login("login")
            .name("Name")
            .birthday(LocalDate.of(2020, 10, 10))
            .build();

    @DisplayName("Добавление пользователя")
    @Test
    void createUser() {
        controller.createUser(user);
        assertEquals(1, controller.getAllUsers().size());
    }

    @DisplayName("Обновление пользователя")
    @Test
    void updateUser() {
        User updateUser = User.builder()
                .id(1)
                .email("newemail@yandex.ru")
                .login("newlogin")

                .birthday(LocalDate.of(2021, 11, 11))
                .build();
        controller.createUser(user);
        controller.updateUser(updateUser);

        assertEquals("newemail@yandex.ru", updateUser.getEmail());
        assertEquals("newlogin", updateUser.getLogin());
        assertEquals(LocalDate.of(2021, 11, 11), updateUser.getBirthday());
        assertEquals("newlogin", updateUser.getName());

    }

    @DisplayName("Почта не заполнена")
    @Test
    void emailEmpty() {
        user.setEmail("");
        assertEquals("Некорректная почта",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Некорректная почта")
    @Test
    void emailIncorrect() {
        user.setEmail("emailyandex.ru");
        assertEquals("Некорректная почта",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Логин пустой")
    @Test
    void userLoginEmpty() {
        user.setLogin("");
        assertEquals("Логин не должен быть пустым",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Логин с пробелами")
    @Test
    void userLoginSpace() {
        user.setLogin("Логин логиныч");
        assertEquals("Логин не должен содержать пробелы",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("День рождения не указан")
    @Test
    void userBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2024, 10, 10));
        assertEquals("Некорректный день рождения",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
        assertEquals(0, controller.getAllUsers().size());
    }
}