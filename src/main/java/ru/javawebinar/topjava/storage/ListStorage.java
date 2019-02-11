package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class ListStorage implements Storage {
    private final List<Meal> mealList;

    public ListStorage() {
        this.mealList = MealsUtil.getMeals();
    }

    @Override
    public void create(Meal meal) {
        mealList.add(meal);
    }

    @Override
    public Meal read(int id) {
        return mealList.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public void update(int id, Meal meal) {
        int index = findIndex(id);
        mealList.set(index, meal);
    }

    @Override
    public void delete(int id) {
        int index = findIndex(id);
        mealList.remove(index);
    }

    public List<Meal> getMealList() {
        return mealList;
    }

    private int findIndex(int id) {
        int index = -1;
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }
}
