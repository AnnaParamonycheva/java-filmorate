import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

class FilmControllerTest {
    @Autowired
    //private final FilmController controller = new FilmController();
    private FilmController controller;
    private final Film film = Film.builder()
            .id(1)
            .name("Какой-то фильм")
            .description("Описание какого-то фильма")
            .releaseDate(LocalDate.of(2023, 9, 1))
            .duration(90)
            .build();

    @DisplayName("Добавление фильма")
    @Test
    void create_Film() {
        controller.createFilm(film);
        assertEquals(1, controller.getAllFilms().size());
    }

    @DisplayName("Обновление фильма")
    @Test
    void update_Film() {
        Film updateFilm = Film.builder()
                .id(1)
                .name("Измененное имя")
                .description("Другое описание какого-то фильма")
                .releaseDate(LocalDate.of(2023, 9, 2))
                .duration(120)
                .build();
        controller.createFilm(film);
        controller.updateFilm(updateFilm);

        assertEquals("Другое описание какого-то фильма", updateFilm.getDescription());
        assertEquals("Измененное имя", updateFilm.getName());
        assertEquals(LocalDate.of(2023, 9, 2), updateFilm.getReleaseDate());
        assertEquals(120, updateFilm.getDuration());
        assertEquals(1, controller.getAllFilms().size());
    }

    @DisplayName("Фильм без названия")
    @Test
    void create_EmptyName() {
        film.setName("");

        assertEquals("Название фильма не указано", assertThrows(ValidationException.class, () ->
                controller.createFilm(film)).getMessage());
        assertEquals(0, controller.getAllFilms().size());
    }

    @DisplayName("Фильм c очень длинным названием")
    @Test
    void create_DescriptionMoreThan200() {
        film.setDescription("Очень длинное название фильма, которое не должно вместиться " +
                "в поле для этого предназначенное. Но всё равно попытаемся его добавить, потому что" +
                "нужно как-то проверить работу с очень длинным описанием. Вроде уже более 200 символов. Поехали");

        assertEquals("Описание должно быть не более 200 символов",
                (assertThrows(RuntimeException.class, () -> controller.createFilm(film)).getMessage()));
        assertEquals(0, controller.getAllFilms().size());
    }

    @DisplayName("Некорректная дата релиза")
    @Test
    void create_WrongDateRelease() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        assertEquals("Некорректная дата релиза",
                ((assertThrows(RuntimeException.class, () -> controller.createFilm(film))).getMessage()));
        assertEquals(0, controller.getAllFilms().size());
    }

    @DisplayName("Длительность фильма отрицательная")
    @Test
    void create_WrongDuration() {
        film.setDuration(-10);

        assertEquals("Длительность фильма должна быть не менее 1 минуты",
                ((assertThrows(RuntimeException.class, () -> controller.createFilm(film))).getMessage()));
        assertEquals(0, controller.getAllFilms().size());
    }
}