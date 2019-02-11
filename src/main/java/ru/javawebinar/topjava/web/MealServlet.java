package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.ListStorage;
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
    private ListStorage storage;
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = new ListStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if (action == null) {
            List<MealTo> mealToList = MealsUtil.getFilteredWithExcess(storage.getMealList(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealsList", mealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        switch (action) {
            case "edit":
                meal = storage.read(Integer.parseInt(id));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("edit.jsp").forward(request, response);
                break;
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                break;
            case "create":
                request.getRequestDispatcher("create.jsp").forward(request, response);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        String paramCalories = request.getParameter("calories");
        int calories = ("".equals(paramCalories)) ? 0 : Integer.parseInt(paramCalories);

        if (id == null) {
            storage.create(new Meal(localDateTime, description, calories));
        } else {
            Meal meal = new Meal(localDateTime, description, calories);
            storage.update(Integer.parseInt(id), meal);
        }
        response.sendRedirect("meals");
    }
}
