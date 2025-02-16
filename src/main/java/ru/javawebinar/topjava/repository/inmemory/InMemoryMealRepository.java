package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        Random rand = new Random();
        MealsUtil.meals.forEach(meal -> save(meal, /*rand.nextInt(8)*/1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        synchronized (meal) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                meal.setUserId(SecurityUtil.authUserId());
                mealsMap.put(meal.getId(), meal);
                return meal;
            }
        }

        return MealsUtil.isAuthorizedUserMeal(meal, userId) ?
                mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) :
                null;
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        return MealsUtil.isAuthorizedUserMeal(mealsMap.get(id), userId)
                && mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(Integer id, Integer userId) {
        Meal meal = mealsMap.get(id);
        return MealsUtil.isAuthorizedUserMeal(meal, userId) ? meal : null;
    }

    @Override
    public Collection<MealTo> getAll(Integer userId, Integer calories) {
        return MealsUtil.getTos(
                MealsUtil.getFilteredByUser(mealsMap.values(), userId),
                calories);

    }

    public Collection<MealTo> getAllFiltered(Integer userId,
                                             Integer calories,
                                             LocalDate fromDate,
                                             LocalTime fromTime,
                                             LocalDate toDate,
                                             LocalTime toTime) {
        if (fromDate != null && toDate != null) {
            fromTime = fromTime == null ? LocalTime.MIN : fromTime;
            toTime = toTime == null ? LocalTime.MAX : toTime;

            return MealsUtil.getFilteredTos(
                    MealsUtil.getFilteredByUser(mealsMap.values(), userId),
                    Meal::getDateTime,
                    calories,
                    LocalDateTime.of(fromDate, fromTime),
                    LocalDateTime.of(toDate, toTime));
        } else {
            return getAll(userId, calories);
        }
    }

}

