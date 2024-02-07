import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

class UserControllerTest {
    private final UserController controller = new UserController();
    private final User user = User.builder()
            .id(1)
            .email("email@yandex.ru")
            .login("login")
            .name("Name")
            .birthday(LocalDate.of(2020, 10, 10))
            .build();

    @DisplayName("Добавление фильма")
    @Test
    void createUser() {
        controller.createUser(user);
        assertEquals(1, controller.getAllUsers().size());
    }

    @DisplayName("Обновление фильма")
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
        assertThrows(RuntimeException.class, () -> controller.createUser(user));
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Некорректная почта")
    @Test
    void emailIncorrect() {
        user.setEmail("emailyandex.ru");
        assertThrows(RuntimeException.class, () -> controller.createUser(user));
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Логин пустой")
    @Test
    void userLoginEmpty() {
        user.setLogin("");
        assertThrows(RuntimeException.class, () -> controller.createUser(user));
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("Логин с пробелами")
    @Test
    void userLoginSpace() {
        user.setLogin("Логин логиныч");
        assertThrows(RuntimeException.class, () -> controller.createUser(user));
        assertEquals(0, controller.getAllUsers().size());
    }

    @DisplayName("День рождения не указан")
    @Test
    void userBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2024, 10, 10));
        assertThrows(RuntimeException.class, () -> controller.createUser(user));
        assertEquals(0, controller.getAllUsers().size());
    }
}