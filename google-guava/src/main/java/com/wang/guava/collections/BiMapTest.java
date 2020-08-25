package com.wang.guava.collections;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

/**
 * @description: Guava BiMap：Value不允许重复
 * @author: wei·man cui
 * @date: 2020/8/25 11:13
 */
public class BiMapTest {
    /**
     * BiMap特性：不允许Value重复
     */
    @Test
    public void testSpecial() {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        assertThat(biMap.containsValue("2"), equalTo(true));
        try {
            biMap.put("3", "2");
            fail("should not process here.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反转 Key-Value键值对
     */
    @Test
    public void testInverse() {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.put("5", "7");
        biMap.put("8", "12");
        System.out.println(biMap);
        BiMap<String, String> inverse = biMap.inverse();
        System.out.println(inverse);
    }

    @Test
    public void testCreateAndForcePut(){
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.forcePut("2","2");
        System.out.println(biMap);
    }
}
