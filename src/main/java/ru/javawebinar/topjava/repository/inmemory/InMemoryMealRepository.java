package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
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
        Random rand = new Random();
        MealsUtil.meals.forEach(meal -> save(meal, rand.nextInt(2) + 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealsMap.put(meal.getId(), meal);
            return meal;
        }

        return isAuthorizedUserMeal(meal, userId) ?
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
        return getAll(userId);
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

