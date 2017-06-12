package com.tdl.study.pump.core;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.tdl.study.pump.core.parallel.Parals.eachs;
import static com.tdl.study.pump.core.utils.Streams.list;
import static com.tdl.study.pump.core.utils.Streams.spatialMap;

public interface Input<V> extends IO, Dequeue<V>, Supplier<V>, Iterable<V> {
    static Input<?> NULL = (using, batchSize) -> 0;

    @Override
    default long size() {
        return Long.MAX_VALUE;
    }

    @Override
    default long capacity() {
        return 0;
    }

    default <V1> Input<V1> then(Function<V, V1> conv) {
        return Wrapper.warp(this, (using, batchSize) -> dequeue(s -> using.apply(s.map(conv)), batchSize));
    }

    default <V1> Input<V1> thens(Function<Iterable<V>, Iterable<V1>> conv) {
        return thens(conv, 1);
    }

    default <V1> Input<V1> thens(Function<Iterable<V>, Iterable<V1>> conv, int parallelism) {
        return Wrapper.warp(this, (using, batchSize) -> dequeue(s ->
                eachs(list(spatialMap(s, parallelism, t -> conv.apply(() -> Its.it(t)).spliterator())), s1 -> using.apply(s1)), batchSize));
    }


}
