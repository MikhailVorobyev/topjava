package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<UserMealWithExceed> exceedsByLoop = getFilteredWithExceededByLoop(mealList, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : exceedsByLoop) {
            System.out.println(userMealWithExceed.getDateTime() + "  " + userMealWithExceed.getDescription() + "  "
                    + userMealWithExceed.isExceed());
        }

        List<UserMealWithExceed> exceedsByStream = getFilteredWithExceededByStream(mealList, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : exceedsByLoop) {
            System.out.println(userMealWithExceed.getDateTime() + "  " + userMealWithExceed.getDescription() + "  "
                    + userMealWithExceed.isExceed());
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByLoop(List<UserMeal> mealList, LocalTime startTime,
                                                                         LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> returnList = new ArrayList<>();
        Map<LocalDate, Integer> daysCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            int calories = userMeal.getCalories();
            daysCalories.merge(localDate, calories, Integer::sum);
        }

        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                returnList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), daysCalories.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return returnList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime,
                                                                           LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(userMeal ->
                        userMeal.getDateTime().toLocalDate()))
                .values().stream()
                .map(list -> list.stream()
                        .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                        .map(userMeal ->
                                new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                                        userMeal.getCalories(), list.stream().map(UserMeal::getCalories)
                                        .mapToInt(i -> i).sum() > caloriesPerDay))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
