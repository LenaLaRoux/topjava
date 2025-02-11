package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDao {
    public void add(Meal meal) {
        CopyOnWriteArrayList<Meal> mealList = MealsUtil.getMealList();
        mealList.add(meal);
    }

    public void delete(Integer id) {
        if (id == null)
            return;

        int index = findIndexById(id);
        if (index >= 0) {
            MealsUtil.getMealList().remove(index);
        }
    }

    private int findIndexById(Integer id) {
        CopyOnWriteArrayList<Meal> mealList = MealsUtil.getMealList();
        for (int i = 0; i < mealList.size(); i++) {
            if (id.equals(mealList.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    public void update(Meal meal) {
        int index = findIndexById(meal.getId());
        if (index >= 0) {
            MealsUtil.getMealList().set(index, meal);
        }
    }

    public List<Meal> getAll() {
        return MealsUtil.getMealList();
    }

    public Meal getById(Integer id) {
        int index = findIndexById(id);
        if (index >= 0) {
            return MealsUtil.getMealList().get(index);
        }
        return null;
    }
}
