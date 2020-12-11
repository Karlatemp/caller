/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/CallerThreadTrace.java
 */

package io.github.karlatemp.caller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CallerThreadTrace implements CallerImplement {
    private static final Function<Object, StackTraceElement> MF_STE = v -> (StackTraceElement) v;

    @Override
    public StackFrame getCaller() {
        return (StackFrame) process(1, true);
    }

    @Override
    public StackFrame getCaller(int frame) {
        return (StackFrame) process(frame, true);
    }

    @Override
    public List<StackFrame> getTrack() {
        return (List<StackFrame>) process(0, false);
    }

    private static final Function<StackTraceElement, StackFrame> FWX = trace -> new StackFrame(
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
    );

    private Object process(int frame, boolean unique) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // java.base/java.lang.Thread.getStackTrace(Thread.java:1598)
        // io.github.karlatemp.caller.CallerThreadTrace.process(CallerThreadTrace.java:16)
        // io.github.karlatemp.caller.CallerThreadTrace.getCaller(CallerThreadTrace.java:8)
        // io.github.karlatemp.caller.CallerFinder.getCaller(CallerFinder.java:21)
        // io.github.karlatemp.caller.TestUnit.test(TestUnit.java:15)
        // io.github.karlatemp.caller.TestUnit$W.a(TestUnit.java:32)
        // io.github.karlatemp.caller.TestUnit.main(TestUnit.java:22)
        Stream<StackTraceElement> stream = Stream.of(stackTrace)
                .filter(trace -> ReflectionHelper.isNotReflectionClass(trace.getClassName()))
                .skip(4 + frame);
        if (unique)
            return stream.findFirst().map(FWX).orElse(null);
        return stream.map(FWX).collect(Collectors.toList());
    }
}
