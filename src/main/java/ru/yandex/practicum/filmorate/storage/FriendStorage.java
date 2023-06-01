package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;

public interface FriendStorage {
    void addNewFriend(Friend friend);

    void removeFriend(Friend friend);
}
