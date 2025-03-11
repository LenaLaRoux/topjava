package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.getReferenceById(userId);
        meal.setUser(user);
        if (meal.isNew()){
            return crudRepository.save(meal);
        }

        return get(meal.getId(), userId) == null ? null : crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) > 0;
    }

    @Override
    @EntityGraph(attributePaths = {"user"})
    public Meal get(int id, int userId) {
        Optional<Meal> mealOpt = crudRepository.findById(id);
        if (mealOpt.isPresent()) {
            Meal meal = mealOpt.get();
            if (mealOpt.map(Meal::getUser).map(User::getId).stream().anyMatch(uId -> uId == userId)) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
