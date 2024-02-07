package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorFilmId = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Вывод списка фильмов");
        return films.values();
    }

    @ResponseBody
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        log.info("Попытка добавления фильма: {}", film);
        film.setId(generateFilmId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @ResponseBody
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        log.info("Попытка обновления фильма: {}", film);
        Integer id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("Фильм обновлен: {}", film);

        } else {
            log.info("Фильм не найден: {}", film);
            throw new ValidationException("Фильм не найден");
        }
        return film;
    }

    public int generateFilmId() {
        return ++generatorFilmId;
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза");
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Название фильма не указано");
            throw new ValidationException("Название фильма не указано");
        }
        if (film.getDuration() <= 0) {
            log.warn("Длительность фильма должна быть не менее 1 минуты");
            throw new ValidationException("Длительность фильма должна быть не менее 1 минуты");
        }
        if (film.getDescription().length() > 200 || film.getDescription().isEmpty()) {
            log.warn("Описание должно быть не более 200 символов");
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
    }
}
