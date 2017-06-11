package com.tdl.study.pump.core.base;

public interface Named {
    default String name() {
        return getClass().getSimpleName();
    }
}
