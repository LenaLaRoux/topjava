package ru.javawebinar.topjava.web;

import org.apache.commons.lang3.StringUtils;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MealServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealDao mealDao;

    public MealServlet() {
        mealDao = new MealDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = StringUtils.defaultIfEmpty(request.getParameter("action"), "list");
        int id = getIntFromStr(request.getParameter("id"));
        String forward;

        switch (action) {
            case "delete":
                mealDao.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                Meal newMeal = new Meal(LocalDateTime.now(), "", 100);
                request.setAttribute("meal", newMeal);
                forward = INSERT_OR_EDIT;
                break;
            case "edit":
                Meal meal = mealDao.getById(id);
                request.setAttribute("meal", meal);
                forward = INSERT_OR_EDIT;
                break;
            case "list":
            default:
                request.setAttribute("meals", getListMealTo(mealDao.getAll()));
                forward = LIST_MEAL;
        }

        request.setAttribute("formatter", TimeUtil.FORMATTER);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    private List<MealTo> getListMealTo(Map<Integer, Meal> meals) {
        return MealsUtil.filteredByStreams(meals.values(), MealsUtil.CALORIES_PRE_DAY);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        int id = getIntFromStr(request.getParameter("id"));

        String description = request.getParameter("description");
        int calories = getIntFromStr(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));

        Meal meal = new Meal(id, dateTime, description, calories);
        mealDao.addOrUpdate(meal);

        response.sendRedirect("meals");
    }

    private int getIntFromStr(String integerValue) {
        return Integer.parseInt(integerValue);
    }
}
