package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private User userTest = new User(
			1,
			"aa@bb.ru",
			"login",
			"name",
			LocalDate.of(1993, 2, 4)
	);
	private User updatedUserTest = new User(
			1,
			"aa@bbUPD.ru",
			"loginUPD",
			"nameUPD",
			LocalDate.of(1993, 2, 4)
	);

	@Test
	public void createNewUserTest() {
		Optional<User> userOptional = Optional.ofNullable(userStorage.addNewUser(userTest));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", "name")
				);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "login")
				);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("email", "aa@bb.ru")
				);
	}

	@Test
	public void findUserByIdTest() {
		Optional<User> userOptional = Optional.ofNullable(userStorage.addNewUser(userTest));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void updateUserTest() {
		userStorage.addNewUser(userTest);
		userStorage.updateUser(updatedUserTest);
		Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(updatedUserTest));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", "nameUPD")
				);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "loginUPD")
				);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("email", "aa@bbUPD.ru")
				);
		}

	private Film filmTest = new Film(
			1,
			"film",
			"description",
			LocalDate.of(2022, 2, 4), 100);


	private Film updatedFilmTest = new Film(
			1,
			"filmUPD",
			"descriptionUPD",
			LocalDate.of(2022, 2, 4), 100);

	@Test
	public void addFilmTest() {
		filmTest.setMpa(new Mpa(4, "R"));
		filmStorage.addNewFilm(filmTest);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addNewFilm(filmTest));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "film")
				);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("description", "description")
				);
	}

	@Test
	public void updateFilmTest() {
		filmTest.setMpa(new Mpa(4, "R"));
		filmStorage.addNewFilm(filmTest);
		updatedFilmTest.setMpa(new Mpa(4, "R"));

		filmStorage.updateFilm(updatedFilmTest);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(updatedFilmTest));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "filmUPD")
				);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("description", "descriptionUPD")
				);
	}
}