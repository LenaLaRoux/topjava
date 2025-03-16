package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

public abstract class UserServiceTest extends AServiceTest<User> {
    @Autowired
    private UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
        entityCreator = service::create;
        entityGetter = service::get;
        entityDeletor = service::delete;
        entityUpdater = service::update;
        matcher = USER_MATCHER;
    }

    @Override
    User getNew() {
        return UserTestData.getNew();
    }

    @Override
    Integer getAnyEntityId() {
        return USER_ID;
    }

    @Override
    Integer getNotFoundId() {
        return NOT_FOUND;
    }

    @Override
    User getDuplicateUniqueKey() {
        return new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER);
    }

    @Override
    User getUpdated() {
        return UserTestData.getUpdated();
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }


    @Test
    public void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }
}