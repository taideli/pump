package com.tdl.study.pump.core.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Loggable {
    // TODO 这样写的技巧是什么？
    final static Map<Class<? extends Loggable>, Logger> LOGGERS = new ConcurrentHashMap<>();

    default Logger logger() {
        return LOGGERS.computeIfAbsent(this.getClass(), Logger::getLogger);
    }
}
