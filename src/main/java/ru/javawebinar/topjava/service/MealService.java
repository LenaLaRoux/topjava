package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(Integer userId, Integer calories) {
        return new ArrayList<>(repository.getAll(userId, calories));
    }

    public List<MealTo> getAllFiltered(Integer userId, Integer calories, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return new ArrayList<>(repository.getAllFiltered(userId, calories, startDate, startTime, endDate, endTime));
    }

    public void update(Meal meal, Integer userId) {
        checkNotFound(repository.save(meal, userId), meal.getId());
    }
}