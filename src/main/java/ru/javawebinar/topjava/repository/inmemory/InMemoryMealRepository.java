package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        Meal mealFound = mealsMap.get(meal.getId());
        return isAuthorizedUserMeal(mealFound, userId) ?
                mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) :
                null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return isAuthorizedUserMeal(mealsMap.get(id), userId)
                && mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealsMap.get(id);
        return isAuthorizedUserMeal(meal, userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFilteredByUser(mealsMap.values(), userId);

    }

    public List<Meal> getAllFiltered(int userId,
                                     LocalDate fromDate,
                                     LocalTime fromTime,
                                     LocalDate toDate,
                                     LocalTime toTime) {
        List<Meal> meals = getAll(userId);

        return meals.stream()
                .filter(meal -> fromDate == null && toDate == null
                        || DateTimeUtil.isBetweenHalfOpen(meal.getDate(), fromDate, toDate))
                .filter(meal -> fromTime == null || toTime == null
                        || DateTimeUtil.isBetweenHalfOpen(meal.getTime(), fromTime, toTime))
                .collect(Collectors.toList());
    }

    private boolean isAuthorizedUserMeal(Meal meal, Integer userId) {
        return meal != null
                && Objects.equals(meal.getUserId(), userId);
    }

    private List<Meal> getFilteredByUser(Collection<Meal> meals, Integer userId) {

        return meals.stream()
                .filter(meal -> userId == null || meal.getUserId().equals(userId))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }

}

