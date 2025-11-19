package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User validUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        validUser = User.builder()
                .email("test@yandex.ru")
                .login("testlogin")
                .name("test User")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Добавление пользователей")
    void testShouldGenerateIncrementalIds() {
        User user1 = userController.create(validUser);
        User user2 = userController.create(validUser.toBuilder()
                .email("test2@yandex.cru")
                .login("testlogin2")
                .build());

        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testShouldUpdateUser() {
        User createdUser = userController.create(validUser);

        User updatedUser = createdUser.toBuilder()
                .name("Updated Name")
                .email("updated@yandex.ru")
                .build();

        User result = userController.update(updatedUser);

        assertEquals("Updated Name", result.getName());
        assertEquals("updated@yandex.ru", result.getEmail());
        assertEquals(createdUser.getId(), result.getId());
    }

    @Test
    @DisplayName("Должно быть сгенерировано исключение, если пользователь не найден")
    void stestShouldThrowExceptionWhenUpdatingNonExistentUser() {
        User nonExistentUser = validUser.toBuilder()
                .id(123L)
                .build();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userController.update(nonExistentUser));

        assertEquals("Пользователь с id = 123 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Пустой список пользователей")
    void testShouldHandleEmptyUserList() {
        Collection<User> users = userController.getUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Имя пустое, должен отображаться логин")
    void testShouldUseLoginWhenNameIsEmpty() {
        User userWithoutName = validUser.toBuilder()
                .name("")
                .build();

        User createdUser = userController.create(userWithoutName);

        assertEquals("testlogin", createdUser.getName());
    }

    @Test
    @DisplayName("Имя null, отображается логин")
    void testShouldUseLoginWhenNameIsNull() {
        User userWithoutName = validUser.toBuilder()
                .name(null)
                .build();

        User createdUser = userController.create(userWithoutName);

        assertEquals("testlogin", createdUser.getName());
    }

    @Test
    @DisplayName("Если имя не null и не пустое, то оно должно отображаться ")
    void testShouldUseActualNameWhenProvided() {
        User userWithName = validUser.toBuilder()
                .name("Name")
                .build();

        User createdUser = userController.create(userWithName);

        assertEquals("Name", createdUser.getName());
    }
}