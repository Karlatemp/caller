/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/CallerThreadTrace.java
 */

package io.github.karlatemp.caller;

import java.util.function.Function;
import java.util.stream.Stream;

class CallerThreadTrace implements CallerImplement {
    private static final Function<Object, StackTraceElement> MF_STE = v -> (StackTraceElement) v;

    @Override
    public StackFrame getCaller() {
        return process(1);
    }

    @Override
    public StackFrame getCaller(int frame) {
        return process(frame);
    }

    private StackFrame process(int frame) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // java.base/java.lang.Thread.getStackTrace(Thread.java:1598)
        // io.github.karlatemp.caller.CallerThreadTrace.process(CallerThreadTrace.java:16)
        // io.github.karlatemp.caller.CallerThreadTrace.getCaller(CallerThreadTrace.java:8)
        // io.github.karlatemp.caller.CallerFinder.getCaller(CallerFinder.java:21)
        // io.github.karlatemp.caller.TestUnit.test(TestUnit.java:15)
        // io.github.karlatemp.caller.TestUnit$W.a(TestUnit.java:32)
        // io.github.karlatemp.caller.TestUnit.main(TestUnit.java:22)
        return Stream.of(stackTrace)
                .filter(trace -> ReflectionHelper.isNotReflectionClass(trace.getClassName()))
                .skip(4 + frame)
                .findFirst()
                .map(trace -> new StackFrame(
                        trace.getClassName(),
                        null,
                        trace.getFileName(),
                        trace.getMethodName(),
                        trace.getLineNumber(),
                        -1,
                        trace.isNativeMethod(),
                        null,
                        null,
                        trace,
                        MF_STE
                ))
                .orElse(null);
    }
}
