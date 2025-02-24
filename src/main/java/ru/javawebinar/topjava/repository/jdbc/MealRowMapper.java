package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ru.javawebinar.topjava.model.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MealRowMapper implements RowMapper<Meal> {
    @Override
    public Meal mapRow(ResultSet rs, int rowNum) throws SQLException {

        Meal meal = new Meal();
        meal.setId(rs.getInt("ID"));
        meal.setDescription(rs.getString("DESCRIPTION"));
        meal.setCalories(rs.getInt("CALORIES"));
        meal.setDateTime(rs.getTimestamp("DATE").toLocalDateTime());

        return meal;

    }
}
