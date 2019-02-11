package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

public interface Storage {
    void create(Meal meal);

    Meal read(int id);

    void update(int id, Meal meal);

    void delete(int id);
}
