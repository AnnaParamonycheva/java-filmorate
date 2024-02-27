package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatorUserId = 0;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        userValidation(user);
        log.info("Попытка добавления пользователя: {}", user);
        user.setId(generateUserId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userValidation(user);
        log.info("Попытка обновления пользователя: {}", user);
        Integer id = user.getId();
        if (!users.containsKey(id)) {
            log.info("Пользователь не найден: {}", user);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        users.put(id, user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    private int generateUserId() {
        return ++generatorUserId;
    }

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорректная почта");
            throw new ValidationException("Некорректная почта");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректный день рождения");
            throw new ValidationException("Некорректный день рождения");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин не должен быть пустым");
            throw new ValidationException("Логин не должен быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не должен содержать пробелы");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }
}