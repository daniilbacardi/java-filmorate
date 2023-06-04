package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mpa {
    @NonNull
    int id;
    String name;

    public Mpa(@NonNull int id, String name) {
        this.id = id;
        this.name = name;
    }
}
