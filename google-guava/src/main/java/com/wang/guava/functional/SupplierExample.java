package com.wang.guava.functional;

import com.google.common.base.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Supplier / Suppliers
 * @date: 2020/7/27 23:32
 * @author: wei·man cui
 */
public class SupplierExample {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Girl {
        int age;
        String face;
    }

    @Test
    public void testSupplier() {
        Supplier<Predicate<String>> supplier = new Supplier<Predicate<String>>() {
            @Override
            public Predicate<String> get() {
                Map<String, Girl> map = new HashMap<>();
                map.put("love the face", new Girl(18, "nice"));
                map.put("love the age", new Girl(16, "ugly"));
                Function<String, Girl> function = Functions.forMap(map);
                Predicate<Girl> predicate = girl -> girl.getAge() >= 18;
                Predicate<String> result = Predicates.compose(predicate, function);
                return result;
            }
        };
        System.out.println(supplier.get().apply("love the age"));
        System.out.println(supplier.get().apply("love the face"));
    }
}
