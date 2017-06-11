package com.tdl.study.pump.core;

import com.tdl.study.pump.core.base.Named;
import com.tdl.study.pump.core.log.Loggable;
import com.tdl.study.pump.core.lambda.Runnable;
import com.tdl.study.pump.core.parallel.Concurrents;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public interface Openable extends AutoCloseable, Loggable, Named {

    enum Status {
        CLOSED, OPENING, OPENED, CLOSING
    }

    default boolean opened() {
        return Opened.status(this).get() == Status.OPENED;
    }

    default boolean closed() {
        return Opened.status(this).get() == Status.CLOSED;
    }

    default void opening(Runnable handler) {
        Opened.OPENING.compute(this,
                (self, orig) -> null == orig ? handler : Runnable.merge(orig, handler));
    }

    default void closing(Runnable handler) {
        Opened.CLOSING.compute(this,
                (self, orig) -> null == orig ? handler : Runnable.merge(orig, handler));
    }

    default void open() {
        AtomicReference<Status> s = Opened.STATUS.computeIfAbsent(this,
                o -> new AtomicReference<>(Status.CLOSED));
        if (s.compareAndSet(Status.CLOSED, Status.OPENING)) {
            logger().trace(name() + " opening");
            Runnable h = Opened.OPENING.get(this);
            if (null != h) h.run();
            if (!s.compareAndSet(Status.OPENING, Status.OPENED))
                throw new RuntimeException("Opened failure since status [" + s.get() + "] not OPENING.");
        }

        if (Status.OPENED != s.get())
            throw new RuntimeException("Start failure since status [" + s.get() + "] not OPENED.");
        logger().trace(name() + " opened");
    }

    @Override
    default void close() {
        AtomicReference<Status> s = Opened.status(this);
        if (s.compareAndSet(Status.OPENED, Status.CLOSING)) {
            logger().trace(name() + " closing...");
            Runnable h = Opened.CLOSING.get(this);
            if (null != h) h.run();
            s.compareAndSet(Status.CLOSING, Status.CLOSED);
        }

        while (!closed()) {
            Concurrents.waitSleep(500, logger(), "Waiting for closing finished...");
            Opened.STATUS.remove(this);
            logger().trace(name() + " closed.");
        }
    }

    class Opened {
        /** 原子操作，防止并发操作读取时值改变*/
        private final static Map<Openable, AtomicReference<Status>> STATUS = new ConcurrentHashMap<>();
        private final static Map<Openable, Runnable> OPENING = new ConcurrentHashMap<>(),
                                                     CLOSING = new ConcurrentHashMap<>();
        /* 禁止实例化*/
        private Opened() {}

        /** 查看类的状态，若未查到，则返回默认状态 {@code Status.CLOSED} */
        private static AtomicReference<Status> status(Openable inst) {
            return STATUS.getOrDefault(inst, new AtomicReference<>(Status.CLOSED));
        }
    }
}
