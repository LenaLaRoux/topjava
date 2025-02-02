package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, MealDayCalories> mealsPerDay = new HashMap<>();

        LocalDate date;

        for (UserMeal meal : meals) {
            date = meal.getDateTime().toLocalDate();
            MealDayCalories mealPerDay = mealsPerDay.getOrDefault(date, new MealDayCalories());
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealPerDay.meals.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            }
            mealPerDay.calories += meal.getCalories();
            mealsPerDay.putIfAbsent(date, mealPerDay);
        }

        for (MealDayCalories mealPerDay : mealsPerDay.values())
        {
            if (mealPerDay.calories > caloriesPerDay)
                result.addAll(mealPerDay.meals);
        }

        return result;
    }

    private static class MealDayCalories {
        List<UserMealWithExcess> meals = new ArrayList<>();
        int calories = 0;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .filter(Objects::nonNull)
                .filter(meal -> meal.getDateTime() != null)
                .collect(Collectors.groupingBy(key -> key.getDateTime().toLocalDate()))
                .values()
                .stream()
                .filter(list -> list.stream().map(UserMeal::getCalories).reduce(0, Integer::sum) > caloriesPerDay)
                .flatMap(Collection::stream)
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true))
                .collect(Collectors.toList());
    }
}
