/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/Caller8.java
 */

package io.github.karlatemp.caller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        return (StackFrame) box(cl(1, true));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StackFrame> getTrack() {
        return (List<StackFrame>) box(cl(0, false));
    }

    @Override
    public StackFrame getCaller(int frame) {
        return (StackFrame) box(cl(frame, true));
    }

    private static final Function<Object, Object> BOX = Caller8::box;

    private static Object box(Object c) {
        if (c == null) return null;
        if (c instanceof Class) {
            return new StackFrame(((Class<?>) c).getName(), (Class<?>) c, null, null, -1, -1, false, null, null, c, TF.INSTANCE);
        }
        return ((Stream<?>) c).map(BOX).collect(Collectors.toList());
    }

    private Object cl(int frame, boolean unique) {
        int c = 0;
        Class<?>[] context = this.getClassContext();
        int pos = 3 + frame;
        //System.out.println(" == " + pos);
        if (pos >= 0) {
            Stream<Class<?>> stream = Stream.of(context)
                    .filter(ReflectionHelper::isNotReflectionClass)
                    .skip(pos);
            if (unique)
                return stream.findFirst().orElse(null);
            return stream;
        }
        return null;
    }
}
