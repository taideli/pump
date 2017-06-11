/**
 * Created by Taideli on 2017/6/11.
 */
package com.tdl.study.pump.core.base;

public class Namedly implements Named {
    protected String name;

    // TODO 这个Named.super.name()是何语法？
    public Namedly() {
        super();
        this.name = Named.super.name();
    }

    public Namedly(String name) {
        super();
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name() + "#" + Integer.toHexString(hashCode());
    }
}
