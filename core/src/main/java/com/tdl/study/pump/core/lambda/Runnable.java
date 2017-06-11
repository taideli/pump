package com.tdl.study.pump.core.lambda;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Runnable extends java.lang.Runnable {

    @Override
    void run();

    /** 让prior在自身之前执行*/
    default Runnable prior(java.lang.Runnable prior) {
        return () -> {
            prior.run();
            run();
        };
    }

    /** 让then先于自身执行*/
    default Runnable then(java.lang.Runnable then) {
        return () -> {
            run();
            then.run();
        };
    }

    /** 直到stopping为真时才执行自身*/
    default Runnable until(Supplier<Boolean> stopping) {
        return () -> {
            while (!stopping.get()) {
                this.run();
            }
        };
    }

    /** 自身执行出错时，执行handler处理异常*/
    default Runnable exception(Consumer<Exception> handler) {
        return () -> {
            try {
                run();
            } catch (Exception e) {
                handler.accept(e);
            }
        };
    }

    /** 依次执行runs*/
    static Runnable merge(java.lang.Runnable... runs) {
        return () -> {
            for (java.lang.Runnable run : runs) {
                if (null != run) run.run();
            }
        };
    }

    /** stopping 为真时，执行run*/
    static Runnable until(java.lang.Runnable run, Supplier<Boolean> stopping) {
        return () -> {
            while (!stopping.get()) {
                run.run();
            }
        };
    }

    /** run运行出错时，执行handler处理异常*/
    static Runnable exception(java.lang.Runnable run, Consumer<Exception> handler) {
        return () -> {
            try {
                run.run();
            } catch (Exception e) {
                handler.accept(e);
            }
        };
    }
}
