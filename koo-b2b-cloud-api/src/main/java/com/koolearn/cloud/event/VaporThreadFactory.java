package com.koolearn.cloud.event;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class VaporThreadFactory implements ThreadFactory {
    private final boolean daemon;
    private AtomicInteger threadCount = new AtomicInteger(0);
    private final String name;

    public VaporThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name + "-" + threadCount.getAndIncrement());
        t.setDaemon(daemon);
        return t;
    }
}
