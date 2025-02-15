package ru.javawebinar.topjava.controller;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MealController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealDao mealDao;

    public MealController() {
        super();
        mealDao = new MealDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            mealDao.delete(mealId);
            forward = LIST_MEAL;
            request.setAttribute("meals", getListMealTo(mealDao.getAll()));
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealDao.getById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("list")) {
            forward = LIST_MEAL;
            request.setAttribute("meals", getListMealTo(mealDao.getAll()));
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.setAttribute("formatter", MealsUtil.FORMATTER);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    private List<MealTo> getListMealTo(List<Meal> meals) {
        return MealsUtil.filteredByStreams(meals, null, null, MealsUtil.CALORIES_PRE_DAY);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("description");
        String caloriesStr = request.getParameter("calories");
        String dateTimeStr = request.getParameter("dateTime");
        String mealIdStr = request.getParameter("id");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        int calories = Integer.parseInt(caloriesStr);

        if (mealIdStr == null || mealIdStr.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            mealDao.add(meal);
        } else {
            int mealId = Integer.parseInt(mealIdStr);
            Meal meal = mealDao.getById(mealId);
            meal.setCalories(calories);
            meal.setDescription(description);
            meal.setDateTime(dateTime);
            mealDao.update(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("formatter", MealsUtil.FORMATTER);
        request.setAttribute("meals", getListMealTo(mealDao.getAll()));
        view.forward(request, response);
    }

}
