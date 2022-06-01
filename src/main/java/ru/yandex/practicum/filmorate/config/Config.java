package ru.yandex.practicum.filmorate.config;

import java.time.LocalDate;
import java.time.Month;

public class Config {
    public static final LocalDate validateDate = LocalDate.of(1895, Month.DECEMBER, 28);
    public static final int MAX_SIZE_DESCRIPTION = 200;
}
