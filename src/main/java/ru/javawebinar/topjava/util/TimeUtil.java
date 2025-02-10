package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        if (startTime == null && endTime == null)
            return true;
        else if (startTime == null) {
            return lt.isBefore(endTime);
        } else if (endTime == null) {
            return !lt.isBefore(startTime);
        }
        else {
            return !lt.isBefore(startTime) && lt.isBefore(endTime);
        }
    }
}
