package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Map;

public class TimingStatisticsRule implements TestRule {
    Map<String, Long> map;

    public TimingStatisticsRule(Map<String, Long> map) {
        this.map = map;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                try {
                    statement.evaluate();
                } finally {
                    long duration = System.currentTimeMillis() - startTime;
                    map.put(description.getMethodName(), duration);
                }
            }

        };
    }
}
