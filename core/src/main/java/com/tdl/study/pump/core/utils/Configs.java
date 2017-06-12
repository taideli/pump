package com.tdl.study.pump.core.utils;

import com.tdl.study.pump.core.log.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Configs {


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Config {
        String value() default "";
        String prefix() default "";
    }

    public static String get(String key, String def) {
        Logger logger = Logger.getLogger(Configs.class);
        logger.error("Method's body is empty, fix it....");
        return "";
    }
}
