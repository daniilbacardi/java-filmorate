package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @JsonIgnore
    int id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}

