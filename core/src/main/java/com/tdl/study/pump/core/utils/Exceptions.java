/**
 * Created by Taideli on 2017/6/11.
 */
package com.tdl.study.pump.core.utils;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Exceptions {

    private static final ReentrantReadWriteLock METHODS_LOCK = new ReentrantReadWriteLock();
//    private static final Map<Class<? extends Throwable>, Method> WRAPPING_METHODS;
//
//    static {
//        try {
//            WRAPPING_METHODS = Maps.of()
//        }
//    }

    public static RuntimeException wrap(Throwable ex) {
        return wrap(ex, RuntimeException.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Exception> T wrap(Throwable ex, Class<T> expect) {
        ex = unwrap(ex);
        if (expect.isAssignableFrom(ex.getClass())) return (T) ex;
        return Reflections.construct(expect, ex);
    }

    public static Throwable unwrap(Throwable ex) {
        if (null == ex) return null;
        METHODS_LOCK.readLock().lock();
    }

    public interface Code {
        // code for system exceptions.
        String ENCRYPT_CODE = "SYS_500";
        String REFLEC_CODE = "SYS_400";
        String DATA_CODE = "SYS_300";

        // code for business exceptions.
        String AUTH_CODE = "BIZ_100";
        String VALID_CODE = "BIZ_200";
    }

}
