package com.tdl.study.pump.core.utils;

import java.lang.reflect.Constructor;

public class Refs {


    static <T> Constructor<T> getMatchingConstructors(final Class<T> cls, final Class<?>... parameterTypes) {
        if (null == cls) return null;
        try {
            return cls.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Constructor<T> result = null;
        // TODO 方法体未完成
        /*for (Constructor<?> ctor : cls.getDeclaredConstructors()) {
            if (isAssignable(parameterTypes, ctor.getParameterTypes(), true) && null != ctor) {
                setAccessibleWorkaround(ctor);
                if (null == result || compareParameterTypes(ctor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
                    result = (Constructor<T>) ctor;
                }
            }
        }*/
        return result;
    }

    static Class<?>[] toClass(final Object... array) {
        if (null == array) return null;
        else if (0 == array.length) return new Class[0];

        final Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = null == array[i] ? null : array[i].getClass();
        }
        return classes;
    }
}
