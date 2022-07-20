package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    void testFindAllUser() {
        List<User> users = userStorage.findAll();

        Assertions.assertThat(users)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void testFindUserById() {
        User user = userStorage.findById(1L);

        Assertions.assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testUpdateUser() {
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setName("updateTest");
        updateUser.setLogin("updateTest");
        updateUser.setEmail("update@update.update");
        updateUser.setBirthday(LocalDate.now());

        User savedUser = userStorage.update(updateUser);

        Assertions.assertThat(savedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "updateTest");
    }

    @Test
    void testAddUser() {
        User user = new User();
        user.setName("test");
        user.setLogin("test");
        user.setEmail("test@test.test");
        user.setBirthday(LocalDate.now());

        User savedUser = userStorage.add(user);

        Assertions.assertThat(savedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testFindAllFilms() {
        List<Film> films = filmStorage.findAll();

        Assertions.assertThat(films)
                .isNotNull()
                .hasSize(1);
    }

//    @Test
//    void testFindFilms() {
//        List<Film> films = filmStorage.findPopularFilms(10);
//
//        Assertions.assertThat(films)
//                .isNotNull()
//                .hasSize(1);
//    }

    @Test
    void testFindFilmById() {
        Film filmOptional = filmStorage.findById(1L);

        Assertions.assertThat(filmOptional)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testUpdateFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);

        Film updateFilm = new Film();
        updateFilm.setId(1L);
        updateFilm.setName("updateTest");
        updateFilm.setDescription("updateTest");
        updateFilm.setReleaseDate(LocalDate.now());
        updateFilm.setDuration(99L);
        updateFilm.setMpa(mpa);

        Film savedFilm = filmStorage.update(updateFilm);

        Assertions.assertThat(savedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "updateTest");
    }

    @Test
    void testAddFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);

        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(99L);
        film.setMpa(mpa);

        Film savedFilm = filmStorage.add(film);

        Assertions.assertThat(savedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }
}
