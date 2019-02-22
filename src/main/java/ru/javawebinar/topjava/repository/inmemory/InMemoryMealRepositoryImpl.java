package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.UsersUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, UsersUtil.USER));
        MealsUtil.ADMIN_MEALS.forEach(meal -> save(meal, UsersUtil.ADMIN));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, HashMap::new);
            repository.get(userId).put(meal.getId(), meal);
        }
        // treat case: update, but absent in storage
        repository.get(userId).computeIfPresent(meal.getId(), (key, oldValue) -> meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> mealsMap = getMap(userId);
        if (mealsMap != null) {
            mealsMap.remove(id);
        }
        return mealsMap != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Map<Integer, Meal> mealsMap = getMap(userId);
        return mealsMap != null ? mealsMap.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        Map<Integer, Meal> mealsMap = getMap(userId);
        return mealsMap != null ? doFilter(userId, meal -> true) : null;
    }

    @Override
    public List<Meal> getFilteredAll(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFilteredAll  startTime {} endTime {} userId {}", startDate, endDate, userId);
        Map<Integer, Meal> mealsMap = getMap(userId);
        return mealsMap != null
                ? doFilter(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                : null;
    }

    private List<Meal> doFilter(int userId, Predicate<Meal> predicate) {
        return repository.get(userId).values()
                .stream()
                .filter(predicate)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMap(int userId) {
        return repository.get(userId);
    }
}

