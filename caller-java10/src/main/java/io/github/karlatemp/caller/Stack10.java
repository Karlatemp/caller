/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/Stack10.java
 */

package io.github.karlatemp.caller;

import java.util.function.Function;

class Stack10 implements Function<StackWalker.StackFrame, StackFrame> {
    @Override
    public StackFrame apply(StackWalker.StackFrame stackFrame) {
        if (stackFrame == null) return null;
        return new StackFrame(
                stackFrame.getClassName(),
                stackFrame.getDeclaringClass(),
                stackFrame.getFileName(),
                stackFrame.getMethodName(),
                stackFrame.getLineNumber(),
                stackFrame.getByteCodeIndex(),
                stackFrame.isNativeMethod(),
                stackFrame.getDescriptor(),
                stackFrame.getMethodType(),
                stackFrame,
                JW_StackFrame_TF.INSTANCE
        );
    }
}
