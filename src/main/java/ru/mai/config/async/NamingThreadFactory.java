package ru.mai.config.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamingThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String prefix;

    public NamingThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + threadNumber.getAndIncrement());
    }

}

