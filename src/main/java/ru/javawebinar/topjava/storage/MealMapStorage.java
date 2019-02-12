package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMapStorage implements Storage {
    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MealMapStorage() {
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    private Integer getNextId() {
        return nextId.incrementAndGet();
    }

    @Override
    public Meal create(Meal meal) {
        int id = getNextId();
        meal.setId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public Meal read(int id) {
        return meals.get(id);
    }

    @Override
    public void update(int id, Meal meal) {
        meals.replace(id, meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}