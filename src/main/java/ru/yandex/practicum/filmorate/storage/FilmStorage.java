package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    Collection<Film> getAllFilms();

    void addGenresToFilm(int filmId, Set<Genre> genres);

    void removeGenresFromFilm(int filmId);

    Set<Genre> getFilmGenres(Integer filmId);

    List<Film> getPopularFilms(int count);
}