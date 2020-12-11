/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/Caller9.java
 */

package io.github.karlatemp.caller;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

@SuppressWarnings({"deprecation", "unchecked"})
class Caller9 implements CallerImplement {
    private final StackWalker walker;
    private static final Function<StackWalker.StackFrame, StackFrame> stackX;

    static {
        Function<StackWalker.StackFrame, StackFrame> stackXZ;
        try {
            Class<?> Stack10 = Class.forName("io.github.karlatemp.caller.Stack10");
            Constructor<?> constructor = Stack10.getDeclaredConstructor();
            constructor.setAccessible(true);
            stackXZ = (Function<StackWalker.StackFrame, StackFrame>) constructor.newInstance();
        } catch (Throwable ignore) {
            stackXZ = new Stack9();
        }
        stackX = stackXZ;
    }

    Caller9() {
        walker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
    }

    @Override
    public StackFrame getCaller() {
        return (StackFrame) walker.walk(new CLG(1, true));
    }

    @Override
    public List<StackFrame> getTrack() {
        return (List<StackFrame>) walker.walk(new CLG(0, false));
    }

    private static class CLG implements Function<Stream<StackWalker.StackFrame>, Object> {
        private final int frame;
        private final boolean unique;

        public CLG(int frame, boolean unique) {
            this.frame = frame;
            this.unique = unique;
        }

        @Override
        public Object apply(Stream<StackWalker.StackFrame> stream) {
            // io.github.karlatemp.caller.Caller9.getCaller(Caller9.java:37)
            // io.github.karlatemp.caller.Caller9.getCaller(Caller9.java:18)
            // io.github.karlatemp.caller.CallerFinder.getCaller(CallerFinder.java:18)
            // io.github.karlatemp.caller.TestUnit9.a(TestUnit9.java:11)
            // io.github.karlatemp.caller.TestUnit9.main(TestUnit9.java:6)
            Stream<StackWalker.StackFrame> streamX = stream
                    .filter(stackFrame -> ReflectionHelper.isNotReflectionClass(
                            stackFrame.getDeclaringClass()
                    ))
                    .skip(2 + this.frame);
            if (unique) {
                return stackX.apply(streamX.findFirst().orElse(null));
            } else {
                return streamX.map(stackX).collect(Collectors.toList());
            }
        }
    }

    @Override
    public StackFrame getCaller(int frame) {
        return (StackFrame) walker.walk(new CLG(frame, true));
    }

}
