package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        log.info("Вывод списка пользователей");
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }


    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с id: '{}' отправлен", id);
        return userStorage.getUserById(id);
    }

    public List<User> addFriend(int firstId, int secondId) {
        User firstUser = userStorage.getUserById(firstId);
        User secondUser = userStorage.getUserById(secondId);

        if (firstUser == null || secondUser == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с id: %d или с id: %d не существует",
                    firstId, secondId));
        }
        if (userStorage.getUserById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи уже друзья");
        }
        firstUser.getFriends().add(secondId);
        secondUser.getFriends().add(firstId);
        log.info("Пользователи: '{}' и '{}' теперь являются друзьями :)",
                firstUser.getName(),
                secondUser.getName());
        return Arrays.asList(firstUser, secondUser);
    }

    public List<User> removeFriend(int firstId, int secondId) {
        User firstUser = userStorage.getUserById(firstId);
        User secondUser = userStorage.getUserById(secondId);
        if (firstUser == null || secondUser == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с id: %d или с id: %d не существует",
                    firstId, secondId));
        }
        if (!firstUser.getFriends().contains(secondId)) {
            throw new InternalException("Пользователи не являются друзьями");
        }
        firstUser.getFriends().remove(secondId);
        secondUser.getFriends().remove(firstId);
        log.info("Пользователи: '{}' и '{}' больше не друзья",
                firstUser.getName(),
                secondUser.getName());
        return Arrays.asList(firstUser, secondUser);
    }

    public List<User> getFriendsListById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Получен писок друзей пользователя '{}'",
                user.getName());
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {

        User firstUser = userStorage.getUserById(firstId);
        User secondUser = userStorage.getUserById(secondId);
        if (firstUser == null || secondUser == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с id: %d или с id: %d не существует",
                    firstId, secondId));
        }
        log.info("Список общих друзей пользователей: '{}' и '{}' сформирован ",
                firstUser.getName(), secondUser.getName());
        return firstUser.getFriends().stream()
                .filter(friendId -> secondUser.getFriends().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}