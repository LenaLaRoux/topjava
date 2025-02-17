package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

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

    public List<Meal> getAll(Integer userId) {
        return new ArrayList<>(repository.getAll(userId));
    }

    public List<Meal> getAllFiltered(Integer userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return new ArrayList<>(repository.getAllFiltered(userId, startDate, startTime, endDate, endTime));
    }

    public void update(Meal meal, Integer userId) {
        checkNotFound(repository.save(meal, userId), meal.getId());
    }
}