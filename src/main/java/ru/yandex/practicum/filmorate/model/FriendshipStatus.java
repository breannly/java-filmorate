package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
