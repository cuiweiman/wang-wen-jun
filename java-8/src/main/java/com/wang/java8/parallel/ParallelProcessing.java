package com.wang.java8.parallel;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @description: Java8的并行编程 Demo
 * @author: weiman cui
 * @date: 2020/6/30 15:40
 */
public class ParallelProcessing {

    public static void main(String[] args) {
        // System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println("[normalAdd] " + measureSumPerformance(ParallelProcessing::normalAdd, 100_000_000L));
        // System.out.println("[iterateStream] " + measureSumPerformance(ParallelProcessing::iterateStream, 100_000_000L));
        // System.out.println("[parallelStream] " + measureSumPerformance(ParallelProcessing::parallelStream, 100_000_000L));
        // System.out.println("[parallelStream2] " + measureSumPerformance(ParallelProcessing::parallelStream2, 100_000_000L));
        System.out.println("[parallelStream3] " + measureSumPerformance(ParallelProcessing::parallelStream3, 100_000_000L));
    }

    private static long measureSumPerformance(Function<Long, Long> adder, Long limit) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            adder.apply(limit);
            long duration = System.currentTimeMillis() - start;
            if (duration < fastest) {
                fastest = duration;
            }
        }
        return fastest;
    }

    private static long normalAdd(long limit) {
        long result = 0L;
        for (long i = 1L; i <= limit; i++) {
            result += i;
        }
        return result;
    }

    private static long iterateStream(long limit) {
        return Stream.iterate(1L, i -> i + 1).
                limit(limit)
                .reduce(0L, Long::sum);
    }

    /**
     * 开启并行。由于拆箱包箱导致速度慢
     *
     * @param limit
     * @return
     */
    private static long parallelStream(long limit) {
        return Stream.iterate(1L, i -> i + 1)
                .parallel()
                .limit(limit)
                .reduce(0L, Long::sum);
    }

    /**
     * 手动拆箱了，速度还是慢。Stream是效率比较低的，要使用 LongStream.rangeClosed
     *
     * @param limit
     * @return
     */
    private static long parallelStream2(long limit) {
        return Stream.iterate(1L, i -> i + 1)
                .mapToLong(Long::longValue)
                .parallel()
                .limit(limit)
                .reduce(0L, Long::sum);
    }

    /**
     * ArrayList：       Excellent完美
     * LinkedList：      Poor效率低
     * IntStream.range： Excellent完美
     * Stream.iterator： Pool效率低
     * HashSet:         Good良好
     * TreeSet:         Good良好
     *
     * @param limit
     * @return
     */
    private static long parallelStream3(long limit) {
        return LongStream.rangeClosed(1, limit)
                .parallel()
                .reduce(0L, Long::sum);
    }


}
