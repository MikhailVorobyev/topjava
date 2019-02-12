package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    Meal create(Meal meal);

    Meal read(int id);

    void update(int id, Meal meal);

    void delete(int id);

    List<Meal> getAll();
}
