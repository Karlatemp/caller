/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/11/13 19:41:00
 *
 * caller/TestUnitKotlin.kt
 */

package io.github.karlatemp.caller

import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class TestUnitKotlin {
    object OpenTest {
        val c9 = Caller9()
        val c8 = Caller8()
        val ctt = CallerThreadTrace()
        fun List<StackFrame>.xwx() = asSequence().take(4).map {
            it.classInstance ?: it.className
        }.toList()

        fun List<KClass<*>>.Z() = map { it.java }

        @JvmStatic
        fun run(type: String) {
            CallerFinder.setImplement(c9)
            assertSame(CallerFinder.getCaller().classInstance, W1::class.java, "$type - [C9]")
            assertSame(CallerFinder.getCaller(1).classInstance, W1::class.java, "$type - [C9]")
            assertSame(CallerFinder.getCaller(2).classInstance, W2::class.java, "$type - [C9]")
            assertSame(CallerFinder.getCaller(3).classInstance, W3::class.java, "$type - [C9]")
            assertEquals(CallerFinder.getTrace().xwx(), listOf(OpenTest::class, W1::class, W2::class, W3::class).Z())
            CallerFinder.setImplement(c8)
            assertSame(CallerFinder.getCaller().classInstance, W1::class.java, "$type - [C8]")
            assertSame(CallerFinder.getCaller(1).classInstance, W1::class.java, "$type - [C8]")
            assertSame(CallerFinder.getCaller(2).classInstance, W2::class.java, "$type - [C8]")
            assertSame(CallerFinder.getCaller(3).classInstance, W3::class.java, "$type - [C8]")
            assertEquals(CallerFinder.getTrace().xwx(), listOf(OpenTest::class, W1::class, W2::class, W3::class).Z())
            CallerFinder.setImplement(ctt)
            assertEquals(CallerFinder.getCaller().className, W1::class.java.name, "$type - [CTT]")
            assertEquals(CallerFinder.getCaller(1).className, W1::class.java.name, "$type - [CTT]")
            assertEquals(CallerFinder.getCaller(2).className, W2::class.java.name, "$type - [CTT]")
            assertEquals(CallerFinder.getCaller(3).className, W3::class.java.name, "$type - [CTT]")
            assertEquals(CallerFinder.getTrace().xwx(), listOf(OpenTest::class, W1::class, W2::class, W3::class).Z().map { it.name })
        }
    }

    object W1 {
        @JvmStatic
        fun run(type: String) {
            OpenTest.run("$type - [W1] [  DIRECT  ]")
            OpenTest::class.java.getMethod("run", java.lang.String::class.java)
                    .invoke(null, "$type - [W1] [REFLECTION]")
            OpenTest::class.functions.first { it.name == "run" }.call(OpenTest, "$type - [W1] [KOTLIN REF]")
        }
    }

    object W2 {
        @JvmStatic
        fun run(type: String) {
            W1.run("$type - [W2] [  DIRECT  ]")
            W1::class.java.getMethod("run", java.lang.String::class.java)
                    .invoke(null, "$type - [W2] [REFLECTION]")
            W1::class.functions.first { it.name == "run" }.call(W1, "$type - [W2] [KOTLIN REF]")
        }
    }

    object W3 {
        @JvmStatic
        fun run(type: String) {
            W2.run("$type - [W3] [  DIRECT  ]")
            W2::class.java.getMethod("run", java.lang.String::class.java)
                    .invoke(null, "$type - [W3] [REFLECTION]")
            W2::class.functions.first { it.name == "run" }.call(W2, "$type - [W3] [KOTLIN REF]")
        }
    }

    @Test
    fun test() {
        W3.run("[ROOT] [  DIRECT  ]")
        W3::class.java.getMethod("run", java.lang.String::class.java)
                .invoke(null, "[ROOT] [REFLECTION]")
        W3::class.functions.first { it.name == "run" }.call(W3, "[ROOT] [KOTLIN REF]")
    }
}
