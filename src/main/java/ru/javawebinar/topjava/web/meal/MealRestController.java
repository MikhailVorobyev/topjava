package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal.getId());
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllFiltered: startDate {} endDate {} startTime {} endTime {}", startDate, endDate, startTime, endTime);
        LocalDate sd = startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate ed = endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime st = startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime et = endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime);

        List<Meal> filteredByDate = service.getFilteredAll(SecurityUtil.authUserId(), sd, ed);
        return MealsUtil.getFilteredWithExcess(filteredByDate, SecurityUtil.authUserCaloriesPerDay(), st, et);
    }
}