/**
 * Created by Taideli on 2017/6/11.
 */
package com.tdl.study.pump.core.utils;

import com.tdl.study.pump.core.log.Logger;

import java.lang.reflect.Constructor;

public class Reflections {
    private static final Logger logger = Logger.getLogger(Reflections.class);


    public static <T> T construct(final Class<T> cls, Object... parameters) {
        final Class<?> parameterTypes[] = Refs.toClass(parameters);
        return construct(cls, parameters, parameterTypes);
    }

    public static <T> T construct(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) {
        final Constructor<T> ctor = Refs.getMatchingConstructors(cls, parameterTypes);
        if (ctor == null) {
            logger.error("No such constructor on object: " + cls.getName());
            return null;
        }
        if (!ctor.isAccessible()) ctor.setAccessible(true);
        try {
            return ctor.newInstance(args);
        } catch (Exception e) {
            logger.error("Construction failure", Exceptions.unwrap(e));
            return null;
        }
    }
}
