/**
 * Created by Taideli on 2017/6/11.
 */
package com.tdl.study.pump.core;

import com.tdl.study.pump.core.lambda.Runnable;
import com.tdl.study.pump.core.log.Logger;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Wrapper {

    static <T> WrapInput<T> warp(Input<?> base, Dequeue<T> d) {
        WrapInput<T> input = new WrapInput<T>(base) {

            @Override
            public long dequeue(Function<Stream<T>, Long> using, long batchSize) {
                return d.dequeue(using, batchSize);
            }
        };
        return input;
    }

    /** */
    abstract class WrapInput<V> implements Input<V> {

        protected final Input<?> base;

        protected WrapInput(Input<?> origin) {
            base = origin;
        }

        @Override
        public abstract long dequeue(Function<Stream<V>, Long> using, long batchSize);

        @Override
        public long size() {
            return base.size();
        }

        @Override
        public long capacity() {
            return base.capacity();
        }

        @Override
        public boolean empty() {
            return base.empty();
        }

        @Override
        public boolean full() {
            return base.full();
        }

        @Override
        public Logger logger() {
            return base.logger();
        }

        @Override
        public String name() {
            return base.name();
        }

        @Override
        public String toString() {
            return base.toString() + "WrapIn";
        }

        @Override
        public boolean opened() {
            return base.opened();
        }

        @Override
        public boolean closed() {
            return base.closed();
        }

        @Override
        public void opening(Runnable handler) {
            base.opening(handler);
        }

        @Override
        public void closing(Runnable handler) {
            base.closing(handler);
        }

        @Override
        public void open() {
            base.open();
        }

        @Override
        public void close() {
            base.close();
        }
    }
}
