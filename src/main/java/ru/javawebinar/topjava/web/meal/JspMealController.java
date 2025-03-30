package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AMealController{
    private static final String MEALS = "/meals";
    private static final String MEAL_FORM = "mealForm";
    private static final String REDIRECT_MEALS = "redirect:"+MEALS;

    @GetMapping
    public String getMeals(Model model) {
        log.info("meals");
        int userId = SecurityUtil.authUserId();
        List<MealTo> mealsTo = MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealsTo);
        return MEALS;
    }

    @GetMapping
    public ModelAndView filterMeals(HttpServletRequest request) {
        LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));

        List<MealTo> mealToList = getBetween(startDate, startTime, endDate, endTime);

        ModelAndView model = new ModelAndView(MEALS);
        model.addObject("meals", mealToList);
        return model;
    }

    @GetMapping("/update")
    public ModelAndView update(@RequestParam int id) {
        log.info("meals/update {}", id);
        Meal meal = service.get(id, SecurityUtil.authUserId());
        ModelAndView model = new ModelAndView(MEAL_FORM);
        model.addObject("meal", meal);
        return model;
    }

    @GetMapping("/create")
    public String create (Model model) {
        log.info("meals/create");
        model.addAttribute("meal", new Meal());
        return MEAL_FORM;
    }

    @PostMapping(value = "/save")
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        Integer id = getInteger(request, "id");
        log.info("meals/save {}", id);

        LocalDateTime dateTime = DateTimeUtil.parseLocalDateTime(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(id, dateTime, description, calories);

        if (meal.isNew()) {
            service.create(meal, SecurityUtil.authUserId());
        } else {
            service.update(meal, SecurityUtil.authUserId());
        }
        return REDIRECT_MEALS;
    }

    private Integer getInteger(HttpServletRequest request, String param) {
        String parameter = request.getParameter(param);
        return StringUtils.hasLength(parameter) ? Integer.parseInt(parameter) : null;
    }

    @GetMapping("/delete")
    public String deleteMeal(@RequestParam int id) {
        delete(id);
        return REDIRECT_MEALS;
    }
}
