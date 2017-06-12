/**
 * Created by Taideli on 2017/6/11.
 */
package com.tdl.study.pump.core.parallel;

import com.tdl.study.pump.core.base.Namedly;
import com.tdl.study.pump.core.log.Logger;
import com.tdl.study.pump.core.utils.Configs;
import com.tdl.study.pump.core.utils.Exceptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

import static com.tdl.study.pump.core.utils.Exceptions.unwrap;
import static com.tdl.study.pump.core.utils.Exceptions.wrap;

public final class Parals {
    private static final Logger logger = Logger.getLogger(Parals.class);
    private static final String EXECUTOR_NAME = "IoStream";
    private static final String PARALLELISM_FACTOR_KEY = "com.tdl.study.pump.core.parallelism.factor";
    private static final int SYS_PARALLELISM = Exers.detectParallelism();
    private static final Exers EXERS = new Exers(EXECUTOR_NAME, SYS_PARALLELISM, false);


    public static <V> long eachs(Iterable<V> src, Consumer<V> doing) {
        // TODO unfinished method, fix me
        return 0;
    }


    private static final class Exers extends Namedly implements AutoCloseable {

        public static final Logger Logger = com.tdl.study.pump.core.log.Logger.getLogger(Exers.class);
//        final ExecutorService exor;
//        final ListeningExecutorService lexor;
        private static final Map<String, ThreadGroup> g = new ConcurrentHashMap<>();

        public Exers(String name, int parallelism, boolean throwException) {
            Thread.UncaughtExceptionHandler handler = (t, e) -> {
                logger.error("Migrater pool task failure @" + t.getName(), e);
                if (throwException) throw wrap(unwrap(e));
            };
        }

        @Override
        public void close() throws Exception {
            logger.debug(name() + " is closing...");
        }

        private static int detectParallelism() {
            double f = Double.parseDouble(Configs.get(PARALLELISM_FACTOR_KEY, "0"));
            if (f <= 0) return (int) f;
            int p = 16 + (int) Math.round((ForkJoinPool.getCommonPoolParallelism() - 16) * (f - 1));
            if (p <= 2) p = 2;
            logger.debug("parallelism calced result is: " + p);
            return p;
        }
    }
}
