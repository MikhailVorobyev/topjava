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
            System.out.println(userMealWithExceed.getDateTime() + "  " + userMealWithExceed.getDescription());
        }

        List<UserMealWithExceed> exceedsByStream = getFilteredWithExceededByStream(mealList, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : exceedsByStream) {
            System.out.println(userMealWithExceed.getDateTime() + "  " + userMealWithExceed.getDescription());
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByLoop(List<UserMeal> mealList, LocalTime startTime,
                                                                         LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> returnList = null;
        mealList.sort(Comparator.comparing(x -> x.getDateTime().toLocalDate()));

        Map<LocalDate, Integer> daysCalories = new HashMap<>();
        LocalDate localDate = null;
        List<UserMealWithExceed> timeIntervalMealsList = null;
        for (int i = 0; i < mealList.size(); i++) {
            UserMeal meal = mealList.get(i);
            LocalDate currentLocalDate = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();
            daysCalories.merge(currentLocalDate, calories, (previousSum, currentValue) -> previousSum + currentValue);
            if (localDate == null || !localDate.equals(currentLocalDate) || i == mealList.size() - 1) {
                localDate = currentLocalDate;
                if (daysCalories.get(currentLocalDate) > caloriesPerDay) {
                    returnList = timeIntervalMealsList;
                }
                timeIntervalMealsList = new ArrayList<>();
            }
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                timeIntervalMealsList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), true));
            }
        }
        return returnList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime,
                                                                           LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(userMeal ->
                        userMeal.getDateTime().toLocalDate()))
                .entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(list -> list.stream().map(UserMeal::getCalories).mapToInt(i -> i).sum() > caloriesPerDay)
                .flatMap(Collection::stream)
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), true))
                .collect(Collectors.toList());
    }
}
