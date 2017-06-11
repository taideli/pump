package com.tdl.study.pump.core.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
    private static final boolean async;
    private static final AtomicInteger tn;
    private static final ThreadGroup g;
    private static final ExecutorService logex;
    private final org.slf4j.Logger logger;
    private static final String ASYNC_ENABLE_PROPERTY = "com.tdl.study.pump.core.log.async.enable";
    private static final String LOGGER_THREAD_GROUP_NAME = "PumpLoggerThread";

    static {
        async = Boolean.parseBoolean(System.getProperty(ASYNC_ENABLE_PROPERTY, "true"));
        if (async) {
            tn = new AtomicInteger();
            g = new ThreadGroup(LOGGER_THREAD_GROUP_NAME);
            logex = Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(g, r, LOGGER_THREAD_GROUP_NAME + "#" + tn.getAndIncrement());
                t.setDaemon(true);
                return t;
            });
        } else {
            tn = null;
            g = null;
            logex = null;
        }
    }

    private Logger(org.slf4j.Logger logger) {
        super();
        this.logger = logger;
    }

    public static Logger getLogger(CharSequence name) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(name.toString()));
    }


    public static Logger getLogger(Class<? extends Loggable> clazz) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(clazz));
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean trace(CharSequence msg) {
        logger.trace(msg.toString());
        return true;
    }
}
