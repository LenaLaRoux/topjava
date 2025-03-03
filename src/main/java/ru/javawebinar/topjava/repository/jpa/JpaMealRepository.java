package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        setUserRef(meal, userId);
        if (meal.isNew()) {
            entityManager.persist(meal);
            return meal;
        } else {
            Meal persistMeal = get(meal.id(), userId);
            if (persistMeal == null) {
                return null;
            }
            copySrc(persistMeal, meal);
            return entityManager.merge(persistMeal);
        }
    }

    private void copySrc(Meal dest, Meal src) {
        dest.setDateTime(src.getDateTime());
        dest.setDescription(src.getDescription());
        dest.setCalories(src.getCalories());
    }

    private void setUserRef(Meal meal, int userId) {
        User userRef = entityManager.getReference(User.class, userId);
        meal.setUser(userRef);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return entityManager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = entityManager.createNamedQuery(Meal.BY_USER, Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.FILTER, Meal.class)
                .setParameter("userId", userId)
                .setParameter("from", startDateTime)
                .setParameter("to", endDateTime)
                .getResultList();
    }
}