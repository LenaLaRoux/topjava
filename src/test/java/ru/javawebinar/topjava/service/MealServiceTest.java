package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.user.InMemoryAdminRestControllerTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerTest.class);
    private static final Map<String, Long> timeMap = new HashMap<>();

    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {
        @Override
        protected void after() {
            log.debug("Test Execution Summary:");
            for (Map.Entry<String, Long> entry : timeMap.entrySet()) {
                log.debug(rightPad(entry.getKey() + " (ms)", " ", 30) + " - " + entry.getValue());
            }
        }

        private String rightPad(String src, String ch, int length) {
            int rightLength = length - src.length();
            String rightPad = ch.repeat(rightLength);
            return src + rightPad;
        }
    };

    @Rule
    public TestRule timingStatisticsRule = (statement, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            long startTime = System.currentTimeMillis();
            try {
                statement.evaluate();
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                log.debug("Test duration (ms): " + duration);
                timeMap.put(description.getMethodName(), duration);
            }
        }
    };

    @Autowired
    private MealService service;


    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
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