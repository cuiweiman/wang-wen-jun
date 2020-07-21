package com.wang.java8.parallel.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 * @description: Fork Join分而治之
 * @author: wei·man cui
 * @date: 2020/6/30 16:47
 */
public class ForkJoinPoolTest {

    private static int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public static void main(String[] args) {
        System.out.println(calc());

        AccumulatorRecursiveTask task = new AccumulatorRecursiveTask(0, data.length, data);
        ForkJoinPool pool = new ForkJoinPool();
        Integer result = pool.invoke(task);
        System.out.println("RecursiveTask =>  " + result);

        AccumulatorRecursiveAction action = new AccumulatorRecursiveAction(0, data.length, data);
        pool.invoke(action);
        AccumulatorRecursiveAction.AccumulatorHelper.reset();
        System.out.println("RecursiveAction => " + AccumulatorRecursiveAction.AccumulatorHelper.getResult());
    }

    private static int calc() {
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            result += data[i];
        }
        return result;
    }

}
