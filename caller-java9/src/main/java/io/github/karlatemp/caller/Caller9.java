/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/Caller9.java
 */

package io.github.karlatemp.caller;

import java.lang.reflect.Constructor;
import java.util.function.Function;
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
        return walker.walk(new CLG(1));
    }

    private static class CLG implements Function<Stream<StackWalker.StackFrame>, StackFrame> {
        private final int frame;

        public CLG(int frame) {
            this.frame = frame;
        }

        @Override
        public StackFrame apply(Stream<StackWalker.StackFrame> stream) {
            // io.github.karlatemp.caller.Caller9.getCaller(Caller9.java:37)
            // io.github.karlatemp.caller.Caller9.getCaller(Caller9.java:18)
            // io.github.karlatemp.caller.CallerFinder.getCaller(CallerFinder.java:18)
            // io.github.karlatemp.caller.TestUnit9.a(TestUnit9.java:11)
            // io.github.karlatemp.caller.TestUnit9.main(TestUnit9.java:6)
            StackWalker.StackFrame frame = stream
                    .filter(stackFrame -> ReflectionHelper.isNotReflectionClass(
                            stackFrame.getDeclaringClass()
                    ))
                    .skip(2 + this.frame)
                    .findFirst()
                    .orElse(null);
            if (frame == null) return null;
            return stackX.apply(frame);
        }
    }

    @Override
    public StackFrame getCaller(int frame) {
        return walker.walk(new CLG(frame));
    }

}
