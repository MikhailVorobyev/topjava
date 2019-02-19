package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final int USER = 1;
    private static final int ADMIN = 2;

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, USER));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (key, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        boolean check = checkUserMeal(id, userId);
        if (check) {
            repository.remove(id);
        }
        return check;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return checkUserMeal(id, userId) ? repository.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredAll(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFilteredAll  startTime {} endTime {} userId {}", startDate, endDate, userId);
        List<Meal> userListMeal = getAll(userId);
        return userListMeal.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    private boolean checkUserMeal(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() == userId;
    }
}

