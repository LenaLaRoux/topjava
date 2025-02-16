package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtil {
    public static final List<User> users = Arrays.asList(
            new User(1, "Iva", "iva@mail.ru", "secret", Role.USER),
            new User(2, "Mono", "mono@mail.ru", "secret", Role.USER),
            new User(3, "Napi", "napi@mail.ru", "secret", Role.USER),
            new User(4, "Schnappy", "schnappy@mail.ru", "secret", Role.USER),
            new User(5, "Izzy", "izzy@mail.ru", "secret", Role.USER),
            new User(6, "Polly", "polly@mail.ru", "secret", Role.USER),
            new User(7, "Admin", "admin@mail.ru", "admin", Role.ADMIN)
    );
}
