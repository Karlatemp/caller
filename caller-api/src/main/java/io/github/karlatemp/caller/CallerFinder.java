/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/CallerFinder.java
 */

package io.github.karlatemp.caller;

import java.lang.reflect.Constructor;
import java.util.List;

@SuppressWarnings("deprecation")
public class CallerFinder {
    @Deprecated
    private static CallerImplement implement;

    @Deprecated
    public static CallerImplement getImplement() {
        return implement;
    }

    @Deprecated
    public static void setImplement(CallerImplement implement) {
        CallerFinder.implement = implement;
    }

    public static StackFrame getCaller() {
        return implement.getCaller();
    }

    public static StackFrame getCaller(int frame) {
        return implement.getCaller(frame);
    }

    public static List<StackFrame> getTrace() {
        return implement.getTrack();
    }

    private static CallerImplement allocate(String klass) {
        try {
            Constructor<? extends CallerImplement> constructor = Class.forName(klass)
                    .asSubclass(CallerImplement.class)
                    .getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Throwable ignore) {
        }
        return null;
    }

    static {
        for (String path : new String[]{
                "io.github.karlatemp.caller.Caller9",
                "io.github.karlatemp.caller.Caller8",
                "io.github.karlatemp.caller.CallerThreadTrace",
        }) {
            CallerImplement ci = allocate(path);
            if (ci != null) {
                implement = ci;
                break;
            }
        }
        if (implement == null)
            System.err.println("No any implement for CallerFinder");
    }
}
