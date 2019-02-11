package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private static final AtomicInteger nextId = new AtomicInteger(0);

    public static Integer getId() {
        return nextId.incrementAndGet();
    }
}
