package ru.yandex.practicum.filmorate.model;

public enum FriendshipStatus {
    CONFIRMED,
    UNCONFIRMED;

    public static FriendshipStatus findStatus(String str) {
        if (str.equals(CONFIRMED.name()))
            return CONFIRMED;
        else
            return UNCONFIRMED;
    }
}
