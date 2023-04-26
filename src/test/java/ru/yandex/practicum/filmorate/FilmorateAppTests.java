package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controllers.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateAppTests {
	@Autowired
	private FilmController filmController;
	@Autowired
	private UserController userController;

	@Test
	public void addFilmWithNoNameTest() {
		Film film = new Film();
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1995, 2, 15));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmController.addFilm(film));
	}

	@Test
	public void addFilmWithVeryBigDescriptionTest() {
		Film film = new Film();
		film.setName("Title");
		film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin auctor lectus leo, " +
				"sed sollicitudin eros porttitor vel. Nunc a ipsum vel ipsum porttitor placerat sed ac nibh. " +
				"Donec blandit sodales quam.");
		film.setReleaseDate(LocalDate.of(2022, 4, 10));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmController.addFilm(film));
	}

	@Test
	public void addFilmWithIncorrectReleaseDateTest() {
		Film film = new Film();
		film.setName("Title");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		film.setDuration(180);
		assertThrows(ValidationException.class, () -> filmController.addFilm(film));
	}

	@Test
	public void addFilmWithNegativeDurationTest() {
		Film film = new Film();
		film.setName("Title");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(2020, 2, 2));
		film.setDuration(0);
		assertThrows(ValidationException.class, () -> filmController.addFilm(film));
	}

	@Test
	public void addUserWithIncorrectEmailTest() {
		User user = new User();
		user.setName("user");
		user.setEmail("test.ur");
		user.setBirthday(LocalDate.of(2020, 2, 1));
		user.setLogin("login");
		assertThrows(ValidationException.class, () -> userController.addUser(user));
	}

	@Test
	public void addUserWithSpaceInLoginFieldTest() {
		User user = new User();
		user.setName("userwithoutlogin");
		user.setEmail("415@email.ru");
		user.setLogin(" ");
		user.setBirthday(LocalDate.of(2022, 2, 1));
		assertThrows(ValidationException.class, () -> userController.addUser(user));
	}

	@Test
	public void addUserWithSpacesInLoginTest() {
		User user = new User();
		user.setName("name12");
		user.setEmail("new@art.te");
		user.setBirthday(LocalDate.of(2022, 1, 1));
		user.setLogin("oneword twowo rds");
		assertThrows(ValidationException.class, () -> userController.addUser(user));
	}

	@Test
	public void addUserWithoutNameTest() throws ValidationException {
		User user = new User();
		user.setEmail("testname@testsite.te");
		user.setBirthday(LocalDate.of(2012, 2, 12));
		user.setLogin("login");
		userController.addUser(user);
		assertEquals(user.getName(), user.getLogin());
	}

	@Test
	public void addUserWithIncorrectBirthdayTest() {
		User user = new User();
		user.setEmail("territ@mail.ru");
		user.setBirthday(LocalDate.of(2024, 1, 1));
		user.setLogin("login");
		assertThrows(ValidationException.class, () -> userController.addUser(user));
	}
}