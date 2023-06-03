package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addNewUser(User user) {
        UserValidator.validate(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stamt = connection.prepareStatement(
                    "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)" +
                            " VALUES (?, ?, ?, ?)", new String[]{"user_id"});
            stamt.setString(1, user.getEmail());
            stamt.setString(2, user.getLogin());
            stamt.setString(3, user.getName());
            stamt.setDate(4, Date.valueOf(user.getBirthday()));
            return stamt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()) != null) {
            jdbcTemplate.update("UPDATE USERS " +
                            "SET email = ?, login = ?, name = ?, birthday = ? " +
                            "WHERE user_id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS ");
        while (usersRows.next()) {
            allUsers.add(new User(
                    usersRows.getInt("user_id"),
                    usersRows.getString("email"),
                    usersRows.getString("login"),
                    usersRows.getString("name"),
                    Objects.requireNonNull(usersRows.getDate("birthday")).toLocalDate()));
        }
        return allUsers;
    }

    @Override
    public User getUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE user_id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate()
            );
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public List<User> getAllFriends(int id) {
        return jdbcTemplate.query("SELECT FRIEND_ID, EMAIL, LOGIN, NAME, BIRTHDAY" +
                " FROM FRIENDS " +
                "JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID" +
                " WHERE FRIENDS.USER_ID=?", (rs, rowNum) -> new User(
                rs.getInt("friend_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ? and friend_id " +
                "IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> findUserIdAndMakeUser(rs), id, otherId);
    }

    private User findUserIdAndMakeUser(ResultSet rs) throws SQLException {
        int userId = rs.getInt("friend_id");
        return getUserById(userId);
    }
}