package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(Long id);
}
