/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:01
 *
 * caller/ReflectionHelper.java
 */

package io.github.karlatemp.caller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;

public class ReflectionHelper {
    public enum ClassStatus {
        UNKNOWN(false), REFLECTION_CLASS(true), STANDARD_CLASS(false);

        private ClassStatus(boolean asBoolean) {
            this.asBoolean = asBoolean;
        }

        private final boolean asBoolean;

        public boolean asBoolean() {
            return asBoolean;
        }
    }

    public interface ClassStatusChecker {
        ClassStatus checkClassName(String name);

        default ClassStatus checkClass(Class<?> klass) {
            return checkClassName(klass.getName());
        }
    }

    private static final ConcurrentLinkedQueue<ClassStatusChecker> checkers = new ConcurrentLinkedQueue<>();

    public static void registerChecker(ClassStatusChecker checker) {
        if (checker != null) {
            checkers.add(checker);
        }
    }

    public static void unregister(ClassStatusChecker checker) {
        if (checker != null) {
            checkers.remove(checker);
        }
    }

    private static final BiFunction<ClassStatusChecker, Object, ClassStatus>
            VC_CHECK_CLASS_NAME = (c, n) -> c.checkClassName((String) n),
            VC_CHECK_CLASS = (c, cl) -> c.checkClass((Class<?>) cl);

    private static boolean isReflectionClass(
            BiFunction<ClassStatusChecker, Object, ClassStatus> tester,
            Object handle
    ) {
        for (ClassStatusChecker checker : checkers) {
            ClassStatus status = tester.apply(checker, handle);
            if (status != null && status != ClassStatus.UNKNOWN) {
                return status.asBoolean();
            }
        }
        return false;
    }

    public static boolean isReflectionClass(Class<?> klass) {
        return isReflectionClass(VC_CHECK_CLASS, klass);
    }

    public static boolean isReflectionClass(String klass) {
        return isReflectionClass(VC_CHECK_CLASS_NAME, klass);
    }

    public static boolean isNotReflectionClass(Class<?> klass) {
        return !isReflectionClass(VC_CHECK_CLASS, klass);
    }

    public static boolean isNotReflectionClass(String klass) {
        return !isReflectionClass(VC_CHECK_CLASS_NAME, klass);
    }

    static {
        // lambda
        registerChecker(name -> {
            if (name.indexOf('/') > 0) {
                return ClassStatus.REFLECTION_CLASS;
            }
            return ClassStatus.UNKNOWN;
        });

        // reflection
        registerChecker(name -> {
            if (name.startsWith("jdk.internal.reflect.") ||
                    name.startsWith("sun.reflect.") ||
                    name.startsWith("java.lang.reflect.")
            ) {
                return ClassStatus.REFLECTION_CLASS;
            }
            return ClassStatus.UNKNOWN;
        });


        Set<String> kotlinReflection = new HashSet<>(
                Arrays.asList(
                        "kotlin.jvm.internal.CallableReference",
                        "kotlin.jvm.internal.FunctionReference",
                        "kotlin.jvm.internal.FunctionReferenceImpl"
                )
        );
        registerChecker(name -> {
            if (name.startsWith("kotlin.reflect.") ||
                    kotlinReflection.contains(name)
            ) {
                return ClassStatus.REFLECTION_CLASS;
            }
            return ClassStatus.UNKNOWN;
        });
    }
}
