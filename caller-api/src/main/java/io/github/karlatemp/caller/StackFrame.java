/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/StackFrame.java
 */

package io.github.karlatemp.caller;

import java.lang.invoke.MethodType;
import java.util.function.Function;

@SuppressWarnings("unused")
public class StackFrame {
    private final String className;
    private final Class<?> classInstance;
    private final String fileName;
    private final String methodName;
    private final int lineNumber;
    private final boolean isNative;
    private final String descriptor;
    private final MethodType methodType;
    private final Object handle;
    private final Function<Object, StackTraceElement> transform;
    private final int byteCodeIndex;

    @Deprecated
    public StackFrame(
            String className,
            Class<?> classInstance,
            String fileName, // supported on java 9+
            String methodName,  // supported on java 9+
            int lineNumber,  // supported on java 9+
            int byteCodeIndex,  // supported on java 9+
            boolean isNative,  // supported on java 9+
            String descriptor, // supported on java 10+
            MethodType methodType, // supported on java 10+
            Object handle,
            Function<Object, StackTraceElement> transform
    ) {
        this.className = className;
        this.classInstance = classInstance;
        this.fileName = fileName;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.byteCodeIndex = byteCodeIndex;
        this.isNative = isNative;
        this.descriptor = descriptor;
        this.methodType = methodType;
        this.handle = handle;
        this.transform = transform;
    }

    public String getClassName() {
        return className;
    }

    public Class<?> getClassInstance() {
        return classInstance;
    }

    /**
     * Only support on java 9+
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Only support on java 9+
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Only support on java 9+
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Only support on java 9+
     */
    public int getByteCodeIndex() {
        return byteCodeIndex;
    }

    /**
     * Only support on java 9+
     */
    public boolean isNative() {
        return isNative;
    }

    /**
     * Only support on java 10+
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Only support on java 10+
     */
    public MethodType getMethodType() {
        return methodType;
    }

    public Object getHandle() {
        return handle;
    }

    public Function<Object, StackTraceElement> getTransform() {
        return transform;
    }

    public StackTraceElement toStackTraceElement() {
        return transform.apply(handle);
    }

    @Override
    public String toString() {
        return handle.toString();
    }
}
