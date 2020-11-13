/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/CallerImplement.java
 */

package io.github.karlatemp.caller;


/**
 * Internal api / implement
 */
@Deprecated
public interface CallerImplement {
    StackFrame getCaller();

    StackFrame getCaller(int frame);
}
