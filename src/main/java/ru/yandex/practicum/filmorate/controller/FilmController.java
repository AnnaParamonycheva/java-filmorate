package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {
    private List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAllFilms(){
        return films;
    }

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @PutMaping("/film") 
    public Film saveFilm(@RequestBody Film film) {
        if(films.contains(film)) {
            films.
        } else {
            films.add(film);
            return film;
        }
    }
}
