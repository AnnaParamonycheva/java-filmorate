package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAll() {
        log.info("Список фильмов отправлен");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film removeLike(Integer filmId, Integer userId) {
        filmStorage.removeLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopular(int count) {
        log.info("Список популярных фильмов отправлен");
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}