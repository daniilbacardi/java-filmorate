package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @JsonIgnore
    Set<Integer> likes = new HashSet<>();
    @JsonIgnore
    int likeCount;
    int id;
    @NotBlank
    @NotNull
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    @PastOrPresent
    LocalDate releaseDate;
    @Min(0)
    Integer duration;
}
