package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Map;

public class MealDao implements IDao<Meal> {
    public Meal add(Meal meal) {
        MealsUtil.getMealList().put(meal.getId(), meal);
        return meal;
    }

    public void delete(int id) {
        MealsUtil.getMealList().remove(id);
    }

    public Meal update(Meal meal) {
        return MealsUtil.getMealList().computeIfPresent(meal.getId(), (id, old) -> meal);
    }

    public Map<Integer, Meal> getAll() {
        return MealsUtil.getMealList();
    }

    public Meal getById(int id) {
        return MealsUtil.getMealList().get(id);
    }

    public void addOrUpdate(Meal meal) {
        synchronized (this) {
            if (meal == null)
                return;
            Meal foundMeal = getById(meal.getId());
            if (foundMeal == null) {
                add(meal);
            } else {
                update(meal);
            }
        }
    }
}
