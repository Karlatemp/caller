/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:00
 *
 * caller/TestUnit.java
 */

package io.github.karlatemp.caller;

import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("TrivialFunctionalExpressionUsage")
public class TestUnit {
    private static final Caller9 c9 = new Caller9();
    private static final Caller8 c8 = new Caller8();
    private static final CallerThreadTrace ctt = new CallerThreadTrace();
    private static final MethodHandles.Lookup lk = MethodHandles.lookup();

    private static List<?> awx(List<StackFrame> stackFrames) {
        return stackFrames.stream()
                .limit(5)
                .map(frame -> frame.getClassInstance() == null ? frame.getClassName() : frame.getClassInstance())
                .collect(Collectors.toList());
    }

    public static void run(String type) {
        CallerFinder.setImplement(c9);
        Assert.assertSame("CL 9 - " + type, CallerFinder.getCaller().getClassInstance(), Wp1.class);
        Assert.assertSame("CL 9 - " + type, CallerFinder.getCaller(1).getClassInstance(), Wp1.class);
        Assert.assertSame("CL 9 - " + type, CallerFinder.getCaller(2).getClassInstance(), Wp2.class);
        Assert.assertSame("CL 9 - " + type, CallerFinder.getCaller(3).getClassInstance(), Wp3.class);
        Assert.assertEquals("CL 9 - ", awx(CallerFinder.getTrace()), Arrays.asList(TestUnit.class, Wp1.class, Wp2.class, Wp3.class, TestUnit.class));
        CallerFinder.setImplement(c8);
        Assert.assertSame("CL 8 - " + type, CallerFinder.getCaller().getClassInstance(), Wp1.class);
        Assert.assertSame("CL 8 - " + type, CallerFinder.getCaller(1).getClassInstance(), Wp1.class);
        Assert.assertSame("CL 8 - " + type, CallerFinder.getCaller(2).getClassInstance(), Wp2.class);
        Assert.assertSame("CL 8 - " + type, CallerFinder.getCaller(3).getClassInstance(), Wp3.class);
        Assert.assertEquals("CL 8 - ", awx(CallerFinder.getTrace()), Arrays.asList(TestUnit.class, Wp1.class, Wp2.class, Wp3.class, TestUnit.class));
        CallerFinder.setImplement(ctt);
        Assert.assertEquals("CL TT- " + type, CallerFinder.getCaller().getClassName(), Wp1.class.getName());
        Assert.assertEquals("CL TT- " + type, CallerFinder.getCaller(1).getClassName(), Wp1.class.getName());
        Assert.assertEquals("CL TT- " + type, CallerFinder.getCaller(2).getClassName(), Wp2.class.getName());
        Assert.assertEquals("CL TT- " + type, CallerFinder.getCaller(3).getClassName(), Wp3.class.getName());
        Assert.assertEquals("CL TT- ", awx(CallerFinder.getTrace()),
                Arrays.asList(
                        TestUnit.class.getName(),
                        Wp1.class.getName(),
                        Wp2.class.getName(),
                        Wp3.class.getName(),
                        TestUnit.class.getName()
                )
        );
    }

    interface Wfk {
        void run(String type) throws Throwable;
    }

    public static class Wp1 {
        public static void run(String type) throws Throwable {
            TestUnit.run(type + " - [Wp1] [  DIRECT  ]");
            TestUnit.class.getMethod("run", String.class)
                    .invoke(null, type + " - [Wp1] [REFLECTION]");
            lk.findStatic(TestUnit.class, "run", MethodType.methodType(void.class, String.class))
                    .invoke(type + "[Wp1] [  HANDLE  ] - ");
            ((Wfk) TestUnit::run).run(type + "[Wp1] [  LAMBDA  ]");
        }
    }

    public static class Wp2 {
        public static void run(String type) throws Throwable {
            Wp1.run(type + "[Wp2] [  DIRECT  ]");
            Wp1.class.getMethod("run", String.class)
                    .invoke(null, type + "[Wp2] [REFLECTION]");
            lk.findStatic(Wp1.class, "run", MethodType.methodType(void.class, String.class))
                    .invoke(type + "[Wp2] [  HANDLE  ]");
            ((Wfk) Wp1::run).run(type + "[Wp2] [  LAMBDA  ]");
        }
    }

    public static class Wp3 {
        public static void run(String type) throws Throwable {
            Wp2.run(type + "[Wp3] [  DIRECT  ]");
            Wp2.class.getMethod("run", String.class)
                    .invoke(null, type + "[Wp3] [REFLECTION]");
            lk.findStatic(Wp2.class, "run", MethodType.methodType(void.class, String.class))
                    .invoke(type + "[Wp3] [  HANDLE  ]");
            ((Wfk) Wp2::run).run(type + "[Wp3] [  LAMBDA  ]");
        }
    }

    @Test
    public void test() throws Throwable {
        Wp3.run("[ROOT] [  DIRECT  ]");
        Wp3.class.getMethod("run", String.class)
                .invoke(null, "[ROOT] [REFLECTION]");
        lk.findStatic(Wp3.class, "run", MethodType.methodType(void.class, String.class))
                .invoke("[ROOT] [  HANDLE  ]");
        ((Wfk) Wp3::run).run("[ROOT] [  LAMBDA  ]");
    }
}
