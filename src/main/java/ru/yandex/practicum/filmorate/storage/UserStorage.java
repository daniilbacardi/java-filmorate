package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User addNewUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User user);

    User getUserById(int id);

    List<User> getAllFriends(int id);

    List<User> getCommonFriends(int userId, int friendId);

}