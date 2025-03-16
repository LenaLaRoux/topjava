package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class  AServiceTest<T extends AbstractBaseEntity> {
    private static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };
    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @BeforeClass
    public static void clearResult(){
        results.setLength(0);
    }

    public Function<T, T> entityCreator;
    public Function<Integer, T> entityGetter;
    public  Consumer<Integer> entityDeletor;
    public  Consumer<T> entityUpdater;
    public MatcherFactory.Matcher<T> matcher;

    public AServiceTest (){

    };

    @Test
    public void create() {
        T created = entityCreator.apply(getNew());
        int newId = created.id();
        T newEntity = getNew();
        newEntity.setId(newId);
        matcher.assertMatch(created, newEntity);
        matcher.assertMatch(entityGetter.apply(newId), newEntity);
    }

    @Test
    public void delete() {
        entityDeletor.accept(getAnyEntityId());
        assertThrows(NotFoundException.class, () -> entityGetter.apply(getAnyEntityId()));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> entityDeletor.accept(getNotFoundId()));
    }

    @Test
    public void duplicateUniqueKeyCreate() {
        assertThrows(DataAccessException.class, () ->
                entityCreator.apply(getDuplicateUniqueKey()));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> entityGetter.apply(getNotFoundId()));
    }
    @Test
    public void update() {
        T updated = getUpdated();
        entityUpdater.accept(updated);
        matcher.assertMatch(entityGetter.apply(getAnyEntityId()), getUpdated());
    }

    abstract T getNew();
    abstract Integer getAnyEntityId();
    abstract Integer getNotFoundId();
    abstract T getDuplicateUniqueKey();
    abstract T getUpdated();
}
