package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    Optional<T> findOne(String query, Object... params);

    List<T> findMany(String query, Object... params);

    //boolean delete(String query, long id);
    boolean delete(String query, Object... params);

    void update(String query, Object... params);

    long insert(String query, Object... params);
}
