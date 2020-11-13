/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/Caller8.java
 */

package io.github.karlatemp.caller;

import java.util.function.Function;
import java.util.stream.Stream;

class Caller8 extends SecurityManager implements CallerImplement {
    private enum TF implements Function<Object, StackTraceElement> {
        INSTANCE;

        @Override
        public StackTraceElement apply(Object o) {
            Class<?> c = (Class<?>) o;
            return new StackTraceElement(c.getName(), "<unknown>", null, -1);
        }
    }

    @Override
    public StackFrame getCaller() {
        return box(cl(1));
    }

    @Override
    public StackFrame getCaller(int frame) {
        return box(cl(frame));
    }

    private static StackFrame box(Class<?> c) {
        if (c == null) return null;
        return new StackFrame(c.getName(), c, null, null, -1, -1, false, null, null, c, TF.INSTANCE);
    }

    private Class<?> cl(int frame) {
        int c = 0;
        Class<?>[] context = this.getClassContext();
        /*for (Class<?> ck : context) {
            System.out.println((c++) + "\t= " + ck.getName());
        }*/
        int pos = 3 + frame;
        //System.out.println(" == " + pos);
        if (pos >= 0) {
            return Stream.of(context)
                    .filter(ReflectionHelper::isNotReflectionClass)
                    .skip(pos)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
