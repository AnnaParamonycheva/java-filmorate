package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController controller;
    User user = new User(new HashSet<>(), 1, "email@yandex.ru", "login",
            "Name", LocalDate.of(2020, 10, 10));
    User user1 = new User(new HashSet<>(), 2, "emai1l@yandex.ru", "login1",
            "Name1", LocalDate.of(2020, 10, 11));
    User user2 = new User(new HashSet<>(), 2, "emai2l@yandex.ru", "login2",
            "Общий друг", LocalDate.of(2020, 10, 11));


    @DisplayName("Обновление пользователя")
    @Test
    void updateUser() {

        User updateUser = new User(new HashSet<>(), 1, "newemail@yandex.ru", "newlogin",
                "newName", LocalDate.of(2021, 11, 11));
        controller.createUser(user);
        controller.updateUser(updateUser);

        assertEquals("newemail@yandex.ru", updateUser.getEmail());
        assertEquals("newlogin", updateUser.getLogin());
        assertEquals(LocalDate.of(2021, 11, 11), updateUser.getBirthday());
        assertEquals("newName", updateUser.getName());
    }

    @DisplayName("Почта не заполнена")
    @Test
    void emailEmpty() {
        user.setEmail("");
        assertEquals("Некорректная почта",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
    }

    @DisplayName("Некорректная почта")
    @Test
    void emailIncorrect() {
        user.setEmail("emailyandex.ru");
        assertEquals("Некорректная почта",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
    }

    @DisplayName("Логин пустой")
    @Test
    void userLoginEmpty() {
        user.setLogin("");
        assertEquals("Логин не должен быть пустым",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
    }

    @DisplayName("Логин с пробелами")
    @Test
    void userLoginSpace() {
        user.setLogin("Логин логиныч");
        assertEquals("Логин не должен содержать пробелы",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
    }

    @DisplayName("День рождения не указан")
    @Test
    void userBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2024, 10, 10));
        assertEquals("Некорректный день рождения",
                (assertThrows(ValidationException.class, () -> controller.createUser(user))).getMessage());
    }

    @DisplayName("Добавляем друга")
    @Test
    void addFriend() {
        controller.createUser(user);
        controller.createUser(user1);
        controller.addFriend(user.getId(), user1.getId());

        assertEquals(1, user.getFriends().size());
        assertEquals(1, user1.getFriends().size());
    }

    @DisplayName("Удаляем из друзей")
    @Test
    void deleteFriend() {
        controller.createUser(user);
        controller.createUser(user1);
        controller.addFriend(user.getId(), user1.getId());
        controller.removeFriend(user.getId(), user1.getId());

        assertEquals(0, user.getFriends().size());
        assertEquals(0, user1.getFriends().size());
    }

    @DisplayName("Список общих друзей")
    @Test
    void getCommonFriends() {
        controller.createUser(user);
        controller.createUser(user1);
        controller.createUser(user2);
        controller.addFriend(user.getId(), user2.getId());
        controller.addFriend(user1.getId(), user2.getId());
        List<User> commonFriendList = controller.getCommonFriends(user.getId(), user1.getId());
        assertEquals(1, commonFriendList.size());
        commonFriendList = controller.getCommonFriends(user.getId(), user2.getId());
        assertEquals(0, commonFriendList.size());
    }

    @DisplayName("Список друзей")
    @Test
    void getFriends() {
        controller.createUser(user);
        controller.createUser(user1);
        controller.createUser(user2);
        controller.addFriend(user.getId(), user1.getId());
        controller.addFriend(user.getId(), user2.getId());
        List<User> listUserFriends = controller.getFriendsList(user.getId());

        assertEquals(2, listUserFriends.size());
    }
}