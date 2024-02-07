package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Film.
 */
//@Getter
//@Setter
@Date
public class Film {
    private int id;
    @NonNull
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    @Min
    private int duration;
}
