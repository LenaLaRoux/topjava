package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll(Integer userID, Integer calories) {
        log.info("getAll");
        return service.getAll(userID, calories);
    }

    public List<MealTo> getAllFiltered(Integer userId, Integer calories, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAllFiltered");
        return service.getAllFiltered(userId, calories, startDate, startTime, endDate, endTime);
    }

    public Meal get(Integer id, Integer userId) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, Integer userId) {
        log.info("create {}", meal);
        checkIsNew(meal);
        return service.create(meal, userId);
    }

    public void delete(Integer id, Integer userId) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, Integer id, Integer userId) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

}
