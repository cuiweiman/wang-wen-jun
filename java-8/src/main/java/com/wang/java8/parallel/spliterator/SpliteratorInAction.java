package com.wang.java8.parallel.spliterator;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @description: Spliterator接口介绍
 * @author: wei·man cui
 * @date: 2020/6/30 17:31
 */
public class SpliteratorInAction {

    public static void main(String[] args) {
        // spliteratorDemo();

        MySpliteratorTest mySpliterator = new MySpliteratorTest("");
        Optional.ofNullable(mySpliterator.stream().count()).ifPresent(System.out::println);
        mySpliterator.stream().forEach(System.out::println);
        mySpliterator.parallelStream().filter(s -> !("").equals(s)).forEach(System.out::println);
    }

    /**
     * Spliterator接口介绍 使用Demo
     */
    private static void spliteratorDemo() {
        IntStream intStream = IntStream.rangeClosed(0, 10);
        Spliterator.OfInt spliterator = intStream.spliterator();
        Consumer<Integer> consumer = integer -> System.out.println(integer);
        spliterator.forEachRemaining(consumer);
    }

    static class MySpliteratorTest {
        private final String[] data;

        public MySpliteratorTest(String data) {
            Objects.requireNonNull(data, "The parameter cannot be null");
            this.data = data.split("\n");
        }

        public Stream<String> stream() {
            return StreamSupport.stream(new MySpliterator(), false);
        }

        public Stream<String> parallelStream() {
            return StreamSupport.stream(new MySpliterator(), true);
        }

        /**
         * 自定义 Spliterator
         * start,end： String的起始 坐标
         */
        private class MySpliterator implements Spliterator<String> {
            private int start, end;

            public MySpliterator() {
                this.start = 0;
                // 内部类 调用所在类的 内部变量
                this.end = MySpliteratorTest.this.data.length - 1;
            }

            public MySpliterator(int start, int end) {
                this.start = start;
                this.end = end;
            }

            // 流中没有内容，则返回false
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (start <= end) {
                    action.accept(MySpliteratorTest.this.data[start++]);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<String> trySplit() {
                int mid = (end - start) / 2;
                if (mid <= 1) {
                    return null;
                }
                int left = start;
                int right = start + mid;
                start = start + mid + 1;
                return new MySpliterator(left, right);
            }

            @Override
            public long estimateSize() {
                return end - start;
            }

            @Override
            public long getExactSizeIfKnown() {
                return estimateSize();
            }

            @Override
            public int characteristics() {
                return IMMUTABLE | SIZED | SUBSIZED;
            }
        }

    }

}
