package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;


//@Getter
//@Setter
@Date
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;    
    private String name;
    private LocalDateTime birthday;
}
