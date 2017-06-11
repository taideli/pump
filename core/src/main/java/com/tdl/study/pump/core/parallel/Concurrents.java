package com.tdl.study.pump.core.parallel;

import com.tdl.study.pump.core.log.Logger;

public class Concurrents {
    public static long DEFAULT_WAIT_MS = 100;

    public static boolean waitSleep() {
        return waitSleep(DEFAULT_WAIT_MS);
    }

    public static boolean waitSleep(long millis) {
        return waitSleep(millis, null, null);
    }

    public static boolean waitSleep(long millis, Logger logger, CharSequence cause) {
        if (millis < 0) return true;
        try {
            if (null != logger && logger.isTraceEnabled())
                logger.trace("Thread [" + Thread.currentThread().getName() + "] sleep for [" + millis + "ms, cause [" + cause + "].");
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


}
