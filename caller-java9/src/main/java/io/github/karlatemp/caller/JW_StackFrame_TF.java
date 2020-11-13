/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/JW_StackFrame_TF.java
 */

package io.github.karlatemp.caller;

import java.util.function.Function;

enum JW_StackFrame_TF implements Function<Object, StackTraceElement> {
    INSTANCE;

    @Override
    public StackTraceElement apply(Object o) {
        return ((StackWalker.StackFrame) o).toStackTraceElement();
    }
}
