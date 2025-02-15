package ru.javawebinar.topjava.dao;

import java.util.Map;

public interface IDao<T> {
    T add(T meal);

    void delete(int id);

    T update(T meal);

    Map<Integer, T> getAll();

    T getById(int id);

    void addOrUpdate(T item);

}
