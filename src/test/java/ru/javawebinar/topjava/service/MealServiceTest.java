package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@Ignore
public abstract class MealServiceTest extends AServiceTest<Meal> {

    @Autowired
    private MealService service;

    @Before
    public void setup() {
        entityCreator = (newEntity) -> service.create(newEntity, USER_ID);
        entityGetter = (id) -> service.get(id, USER_ID);
        entityDeletor = (id) -> service.delete(id, USER_ID);
        entityUpdater = (entity) -> service.update(entity, USER_ID);
        matcher = MEAL_MATCHER;
    }

    @Override
    Meal getNew() {
        return MealTestData.getNew();
    }

    @Override
    Integer getAnyEntityId() {
        return MEAL1_ID;
    }

    @Override
    Integer getNotFoundId() {
        return NOT_FOUND;
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Override
    Meal getDuplicateUniqueKey() {
        return new Meal(null, meal1.getDateTime(), "duplicate", 100);
    }

    @Override
    Meal getUpdated() {
        return MealTestData.getUpdated();
    }

    @Test
    public void get() {
        Meal actual = service.get(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actual, meal1);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void updateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, exception.getMessage());
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void getAll() {
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void getBetweenInclusive() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), meals);
    }
}