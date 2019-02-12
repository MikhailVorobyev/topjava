package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealMapStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private Storage storage;
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = new MealMapStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if (action == null) {
            List<MealTo> mealToList = MealsUtil.getFilteredWithExcess(storage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealToList", mealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        switch (action) {
            case "edit":
                if (id != null) {
                    meal = storage.read(Integer.parseInt(id));

                } else {
                    meal = Meal.EMPTY;
                    meal = storage.create(meal);
                }
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("edit.jsp").forward(request, response);
                break;
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                break;
            default:
                response.sendRedirect("meals");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        String paramCalories = request.getParameter("calories");
        int calories = ("".equals(paramCalories)) ? 0 : Integer.parseInt(paramCalories);

        Meal updatedMeal = new Meal(localDateTime, description, calories);
        updatedMeal.setId(id);

        storage.update(id, updatedMeal);
        response.sendRedirect("meals");
    }
}
