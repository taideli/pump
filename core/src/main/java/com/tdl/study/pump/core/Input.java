package com.tdl.study.pump.core;

import java.util.function.Supplier;

public interface Input<V> extends IO, Dequeue<V>, Supplier<V>, Iterable<V> {
}
