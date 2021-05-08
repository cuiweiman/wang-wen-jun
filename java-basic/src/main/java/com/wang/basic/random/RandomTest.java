package com.wang.basic.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * java.util.Math
 * {@link Math#random()}： Double 类型，取值范围是 [0.0~1.0]
 * <p>
 * java.util.Random
 * {@link Random#nextInt(int)}：伪随机,当 seed 种子不变时，生成的 随机数 序列 是一样的。
 * {@link Random#next(int)} 使用老种子生成新种子时，do-while 进行了 cas 操作，保证了 线程安全，
 * 但 多线程环境下  竞争和自旋等待的时间 会严重影响 性能。
 * <p>
 * java.util.concurrent.ThreadLocalRandom;
 * {@link ThreadLocalRandom} 每个线程 有一个 对象，不能多线程共享同一个对象(同一个对象生成的随机数相同)；
 *
 * @description: 随机数
 * @author: wei·man cui
 * @date: 2021/5/8 9:49
 */
public class RandomTest {
    public static void main(String[] args) {
        // doubleRandom();
        // randomBasic();
        threadLocalRandomBasic();
    }

    /**
     * {@link ThreadLocalRandom} 每个线程 有一个 对象，不能多线程共享同一个对象(同一个对象生成的随机数相同)；
     */
    public static void threadLocalRandomBasic() {
        for (int i = 0; i < 5; i++) {
            System.out.println("==== 第 " + i + " 次====");
            for (int j = 0; j < 10; j++) {
                new Thread(() -> {
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    System.out.println(Thread.currentThread().getName() + "： " + random.nextInt(50));
                }).start();
            }
        }
    }

    /**
     * {@link Random}：伪随机
     */
    public static void randomBasic() {
        // 默认构造方法：使用当前系统时间的毫秒数作为 seed 种子数
        // final Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.println("==== 第 " + i + " 次====");
            int seed = 100;
            Random random = new Random(seed);
            for (int j = 0; j < 4; j++) {
                System.out.println("index_" + j + "： " + random.nextInt(seed));
            }
        }

    }

    /**
     * Double 类型，取值范围是 [0.0~1.0]
     */
    public static void doubleRandom() {
        final double random = Math.random();
        System.out.println(random);
    }

}
