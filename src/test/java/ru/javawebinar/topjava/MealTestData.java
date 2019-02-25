package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final LocalDateTime START_DATE = LocalDateTime.of(2019, Month.FEBRUARY, 24, 7, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2019, Month.FEBRUARY, 24, 14, 0);

    public static final int USER_MEAL_ID_1 = START_SEQ + 2;
    public static final int USER_MEAL_ID_2 = START_SEQ + 3;
    public static final int USER_MEAL_ID_3 = START_SEQ + 4;

    public static final int ADMIN_MEAL_ID_1 = START_SEQ + 5;
    public static final int ADMIN_MEAL_ID_2 = START_SEQ + 6;
    public static final int ADMIN_MEAL_ID_3 = START_SEQ + 7;

    public static final Meal USER_MEAL_1 = new Meal(
            USER_MEAL_ID_1, LocalDateTime.of(2019, Month.FEBRUARY, 24,  7, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(
            USER_MEAL_ID_2, LocalDateTime.of(2019, Month.FEBRUARY, 24, 13, 0), "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(
            USER_MEAL_ID_3, LocalDateTime.of(2019, Month.FEBRUARY, 24, 20, 0), "Ужин", 510);
    public static final Meal ADMIN_MEAL_1 = new Meal(
            ADMIN_MEAL_ID_1, LocalDateTime.of(2019, Month.FEBRUARY, 25, 8, 0), "Завтрак админа", 500);
    public static final Meal ADMIN_MEAL_2 = new Meal(
            ADMIN_MEAL_ID_2, LocalDateTime.of(2019, Month.FEBRUARY, 25, 12, 30), "Обед админа", 1000);
    public static final Meal ADMIN_MEAL_3 = new Meal(
            ADMIN_MEAL_ID_3, LocalDateTime.of(2019, Month.FEBRUARY, 25, 19, 0), "Ужин админа", 500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
