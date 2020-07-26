package com.wang.guava.functional;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @description: Function api
 * @date: 2020/7/26 10:36
 * @author: wei·man cui
 */
public class FunctionExample {

    public static void main(String[] args) throws IOException {
        // 函数式 逻辑处理
        Function<String, Integer> function = new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String s) {
                Preconditions.checkNotNull(s, "The input String should not be null.");
                return s.length();
            }
        };
        System.out.println(function.apply("Hello"));

        // 面向接口编程，函数式处理
        process("Hello", new Handler.LengthDoubleHandler());

        // Functions.toStringFunction()
        System.out.println(Functions.toStringFunction().apply(new ServerSocket(8888)));

        // String 转 Integer，Integer再转Long
        Function<String, Double> compose = Functions.compose(new Function<Integer, Double>() {
            @Nullable
            @Override
            public Double apply(@Nullable Integer integer) {
                return Double.valueOf(integer);
            }
        }, new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String string) {
                return string.length();
            }
        });
        System.out.println(compose.apply("HelloWorld"));
    }

    interface Handler<IN, OUT> {
        /**
         * 处理输入，并返回类型
         *
         * @param input 输入
         * @return 返回
         */
        OUT handle(IN input);

        class LengthDoubleHandler implements Handler<String, Integer> {
            @Override
            public Integer handle(String input) {
                return input.length() * 2;
            }
        }
    }


    private static void process(String str, Handler<String, Integer> handler) {
        System.out.println(handler.handle(str));
    }

}
