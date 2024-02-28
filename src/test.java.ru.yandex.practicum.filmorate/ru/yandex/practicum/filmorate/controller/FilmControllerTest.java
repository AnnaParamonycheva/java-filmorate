package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;
    @Autowired
    private UserController userController;
    Film film = new Film(1, "Какой-то фильм", "Описание какого-то фильма",
            LocalDate.of(2023, 9, 1), 90, new HashSet<>());
    User user = new User( 1, "email@yandex.ru", "login",
            "Name", LocalDate.of(2020, 10, 10),new HashSet<>());

    @DisplayName("Обновление фильма")
    @Test
    void update_Film() {
        Film updateFilm = new Film(1, "Измененное имя",
                "Другое описание какого-то фильма",
                LocalDate.of(2023, 9, 2), 120, new HashSet<>());

        controller.createFilm(film);
        controller.updateFilm(updateFilm);

        assertEquals("Другое описание какого-то фильма", updateFilm.getDescription());
        assertEquals("Измененное имя", updateFilm.getName());
        assertEquals(LocalDate.of(2023, 9, 2), updateFilm.getReleaseDate());
        assertEquals(120, updateFilm.getDuration());
    }

    @DisplayName("Фильм без названия")
    @Test
    void create_EmptyName() {
        film.setName("");

        assertEquals("Название фильма не указано", assertThrows(ValidationException.class, () ->
                controller.createFilm(film)).getMessage());
    }

    @DisplayName("Фильм c очень длинным названием")
    @Test
    void create_DescriptionMoreThan200() {
        film.setDescription("Очень длинное название фильма, которое не должно вместиться " +
                "в поле для этого предназначенное. Но всё равно попытаемся его добавить, потому что" +
                "нужно как-то проверить работу с очень длинным описанием. Вроде уже более 200 символов. Поехали");

        assertEquals("Описание должно быть не более 200 символов",
                (assertThrows(RuntimeException.class, () -> controller.createFilm(film)).getMessage()));
    }

    @DisplayName("Некорректная дата релиза")
    @Test
    void create_WrongDateRelease() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        assertEquals("Некорректная дата релиза",
                ((assertThrows(RuntimeException.class, () -> controller.createFilm(film))).getMessage()));
    }

    @DisplayName("Длительность фильма отрицательная")
    @Test
    void create_WrongDuration() {
        film.setDuration(-10);
        assertEquals("Длительность фильма должна быть не менее 1 минуты",
                ((assertThrows(RuntimeException.class, () -> controller.createFilm(film))).getMessage()));
    }

    @DisplayName("Добавляем лайк фильму")
    @Test
    void put_AddALikeToFilm() {
        userController.createUser(user);
        controller.createFilm(film);
        controller.addFilmLike(film.getId(), user.getId());

        assertEquals(1, film.getUsersLikes().size());
    }

    @DisplayName("Удаляем лайк")
    @Test
    void delete_RemoveLikeFromFilm() {
        userController.createUser(user);
        controller.createFilm(film);
        controller.addFilmLike(film.getId(), user.getId());
        controller.removeFilmLike(film.getId(), user.getId());
        assertEquals(0, film.getUsersLikes().size());
    }

    @DisplayName("вывод списка популярных фильмов")
    @Test
    void get_PopularMovies() {
        userController.createUser(user);
        controller.createFilm(film);
        controller.addFilmLike(film.getId(), user.getId());
        List<Film> popularMoviesList = controller.getPopular(1);
        assertEquals(1, popularMoviesList.size());
    }
}