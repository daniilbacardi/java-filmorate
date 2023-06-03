package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @JsonIgnore
    int likesCount;
    int id;
    @NotBlank
    @NotNull
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Min(0)
    Integer duration;
    Mpa mpa;
    Set<Genre> genres = new TreeSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
