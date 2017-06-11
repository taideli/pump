package com.tdl.study.pump.core.log;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Logger implements Serializable {
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


    public static Logger getLogger(Class<?> clazz) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(clazz));
    }

    /** 异步执行， 要异步失败，则尝试同步运行*/
    private void submit(Runnable run) {
        if (async) try {
            logex.submit(run);
        } catch (RejectedExecutionException e) {
            run.run();
        } else run.run();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean trace(CharSequence msg) {
        logger.trace(msg.toString());
        return true;
    }

    public boolean trace(Supplier<CharSequence> msg) {
        if (logger.isTraceEnabled()) {
            CharSequence m = msg.get();
            if (null != m) logger.trace(m.toString());
        }
        return true;
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean debug(CharSequence msg) {
        submit(() -> logger.debug(msg.toString()));
        return true;
    }

    public boolean debug(Supplier<CharSequence> msg) {
        if (logger.isDebugEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.debug(msg.toString()));
        }
        return true;
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean info(CharSequence msg) {
        submit(() -> logger.info(msg.toString()));
        return true;
    }

    public boolean info(Supplier<CharSequence> msg) {
        if (logger.isInfoEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.info(msg.toString()));
        }
        return true;
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean warn(CharSequence msg) {
        submit(() -> logger.warn(msg.toString()));
        return true;
    }

    public boolean warn(Supplier<CharSequence> msg) {
        if (logger.isWarnEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.warn(msg.toString()));
        }
        return true;
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean error(CharSequence msg) {
        submit(() -> logger.error(msg.toString()));
        return true;
    }

    public boolean error(Supplier<CharSequence> msg) {
        if (logger.isErrorEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.error(m.toString()));
        }
        return true;
    }

    public boolean debug(Supplier<CharSequence> msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.debug(m.toString(), t));
        }
        return true;
    }

    public boolean info(Supplier<CharSequence> msg, Throwable t) {
        if (logger.isInfoEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.info(m.toString(), t));
        }
        return true;
    }

    public boolean warn(Supplier<CharSequence> msg, Throwable t) {
        if (logger.isWarnEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.warn(m.toString(), t));
        }
        return true;
    }

    public boolean error(Supplier<CharSequence> msg, Throwable t) {
        if (logger.isErrorEnabled()) {
            CharSequence m = msg.get();
            if (null != m) submit(() -> logger.error(m.toString(), t));
        }
        return true;
    }

    public boolean trace(CharSequence msg, Throwable t) {
        submit(() -> logger.trace(msg.toString(), t));
        return true;
    }

    public boolean debug(CharSequence msg, Throwable t) {
        submit(() -> logger.debug(msg.toString(), t));
        return true;
    }

    public boolean info(CharSequence msg, Throwable t) {
        submit(() -> logger.info(msg.toString(), t));
        return true;
    }

    public boolean warn(CharSequence msg, Throwable t) {
        submit(() -> logger.warn(msg.toString(), t));
        return true;
    }

    public boolean error(CharSequence msg, Throwable t) {
        submit(() -> logger.error(msg.toString(), t));
        return true;
    }

    /** Old style */
    public boolean trace(CharSequence format, Object arg) {
        submit(() -> logger.trace(format.toString(), arg));
        return true;
    }

    public boolean trace(CharSequence format, Object arg1, Object arg2) {
        submit(() -> logger.trace(format.toString(), arg1, arg2));
        return true;
    }

    public boolean trace(CharSequence format, Object... arguments) {
        submit(() -> logger.trace(format.toString(), arguments));
        return true;
    }

    public boolean debug(CharSequence format, Object arg) {
        submit(() -> logger.debug(format.toString(), arg));
        return true;
    }

    public boolean debug(CharSequence format, Object arg1, Object arg2) {
        submit(() -> logger.debug(format.toString(), arg1, arg2));
        return true;
    }

    public boolean debug(CharSequence format, Object... arguments) {
        submit(() -> logger.debug(format.toString(), arguments));
        return true;
    }

    public boolean info(CharSequence format, Object arg) {
        submit(() -> logger.info(format.toString(), arg));
        return true;
    }

    public boolean info(CharSequence format, Object arg1, Object arg2) {
        submit(() -> logger.info(format.toString(), arg1, arg2));
        return true;
    }

    public boolean info(CharSequence format, Object... arguments) {
        submit(() -> logger.info(format.toString(), arguments));
        return true;
    }

    public boolean warn(CharSequence format, Object arg) {
        submit(() -> logger.warn(format.toString(), arg));
        return true;
    }

    public boolean warn(CharSequence format, Object... arguments) {
        submit(() -> logger.warn(format.toString(), arguments));
        return true;
    }

    public boolean warn(CharSequence format, Object arg1, Object arg2) {
        submit(() -> logger.warn(format.toString(), arg1, arg2));
        return true;
    }

    public boolean error(CharSequence format, Object arg) {
        submit(() -> logger.error(format.toString(), arg));
        return true;
    }

    public boolean error(CharSequence format, Object arg1, Object arg2) {
        submit(() -> logger.error(format.toString(), arg1, arg2));
        return true;
    }

    public boolean error(CharSequence format, Object... arguments) {
        submit(() -> logger.error(format.toString(), arguments));
        return true;
    }
}
