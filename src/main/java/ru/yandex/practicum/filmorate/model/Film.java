package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import ru.yandex.practicum.filmorate.validator.ReleaseDateContraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не указано")
    private String name;
    @NotNull
    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private String description;
    @NotNull
    @ReleaseDateContraint (message = "Введите дату релиза не ранее 28 декабря 1895 года.")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма должна быть не менее 1 минуты")
    private Integer duration;
}
