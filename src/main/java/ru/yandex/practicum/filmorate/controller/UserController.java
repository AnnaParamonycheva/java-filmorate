package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatorUserId = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Вывод списка пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        log.info("Попытка добавления пользователя: {}", user);
        userValidation(user);
        user.setId(generateUserId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        userValidation(user);
        log.info("Попытка обновления пользователя: {}", user);
        Integer id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("Пользователь обновлен: {}", user);
        } else {
            log.info("Пользователь не найден: {}", user);
            throw new ValidationException("Пользователь не найден");
        }
        return user;
    }

    public int generateUserId() {
        return ++generatorUserId;
    }

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорректная почта");
            throw new ValidationException("Некорректная почта" + user.getId() + "'");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректный день рождения");
            throw new ValidationException("Некорректный день рождения");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.warn("Логин не должен быть пустым");
            throw new ValidationException("Логин не должен быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не должен содержать пробелы");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }

}
