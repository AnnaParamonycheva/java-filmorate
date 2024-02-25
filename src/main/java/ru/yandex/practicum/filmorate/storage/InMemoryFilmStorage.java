package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorFilmId = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
    @Override
    public Film createFilm(Film film) {
        filmValidation(film);
        log.info("Попытка добавления фильма: {}", film);
        film.setId(generateFilmId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidation(film);
        log.info("Попытка обновления фильма: {}", film);
        Integer id = film.getId();
        if (!films.containsKey(id)) {
            log.info("Фильм не найден: {}", film);
            throw new ObjectNotFoundException("Фильм не найден");
        }
        films.put(id, film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }
    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    private int generateFilmId() {
        return ++generatorFilmId;
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза");
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не указано");
            throw new ValidationException("Название фильма не указано");
        }
        if (film.getDuration() <= 0) {
            log.warn("Длительность фильма должна быть не менее 1 минуты");
            throw new ValidationException("Длительность фильма должна быть не менее 1 минуты");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.warn("Описание должно быть не более 200 символов");
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
    }
}