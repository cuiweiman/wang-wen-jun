package com.wang.boot.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.stream.Stream;

/**
 * @description: guava 的布隆过滤器：占用内存
 * @author: wei·man cui
 * @date: 2021/3/11 16:41
 */
public class GuavaBloomFilter {

    public static void main(String[] args) {
        int size = 10000;
        int statistics = 0;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000, 0.01);
        Stream.iterate(0, n -> n + 1).limit(size).forEach(bloomFilter::put);
        System.out.println(bloomFilter.mightContain(1024));
        for (int i = size * 2; i < size * 3; i++) {
            if (bloomFilter.mightContain(i)) {
                statistics++;
            }
        }
        System.out.println("误判次数：" + statistics);
    }

}
