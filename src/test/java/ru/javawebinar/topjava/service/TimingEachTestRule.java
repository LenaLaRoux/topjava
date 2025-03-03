package ru.javawebinar.topjava.service;

import org.junit.rules.ExternalResource;

public class TimingEachTestRule extends ExternalResource {
    long timeStart;

    @Override
    protected void before() {
        timeStart = System.currentTimeMillis();
    }

    protected void after() {
        long duration = System.currentTimeMillis() - timeStart;
        System.out.println("Test duration (ms): " + duration);
    }
}
