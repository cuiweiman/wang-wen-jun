package com.wang.guava.functional;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * @description: Predicate
 * @date: 2020/7/27 23:12
 * @author: wei·man cui
 */
public class PredicateExample {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Girl {
        int age;
        String face;
    }

    @Test
    public void testPredicate1() {
        Predicate<Girl> predicate = new Predicate<Girl>() {
            @Override
            public boolean apply(@Nullable Girl girl) {
                return girl.getAge() >= 18;
            }
        };
        //有个18岁的漂亮姑娘
        Girl girl = new Girl(18, "nice");
        if (predicate.apply(girl)) {
            System.out.println("be my girl friend");
        } else {
            System.out.println("too young to love");
        }
        //输出结果：be my girl friend
    }

    @Test
    public void testPredicate2() {
        Predicate<Girl> agePredicate = input -> input.getAge() >= 18;
        Predicate<Girl> facePredicate = input -> input.getFace().equals("nice");
        Girl girl = new Girl(18, "ugly");

        //and：用于过滤两个Predicate都为true
        Predicate<Girl> predicate = Predicates.and(agePredicate, facePredicate);
        checkOut(predicate.apply(girl));

        //or：用于过滤其中一个Predicate为true
        predicate = Predicates.or(agePredicate, facePredicate);
        checkOut(predicate.apply(girl));

        //not：用于将指定Predicate取反
        Predicate<Girl> noneAgePredicate = Predicates.not(agePredicate);
        predicate = Predicates.and(noneAgePredicate, facePredicate);
        checkOut(predicate.test(girl));

        //compose: Function与Predicate的组合：构造一个测试用Map集合
        Map<String, Girl> map = new HashMap<>();
        map.put("love", new Girl(18, "nice"));
        map.put("miss", new Girl(16, "ugly"));
        Predicate<Girl> predicate1 = Predicates.and(agePredicate, facePredicate);
        Function<String, Girl> function1 = Functions.forMap(map);
        Predicate<String> stringPredicate = Predicates.compose(predicate1, function1);
        System.out.println(stringPredicate.apply("love"));
        System.out.println(stringPredicate.apply("miss"));
    }

    // 判断输出
    private void checkOut(boolean flag) {
        if (flag) {
            System.out.println("She fits me");
        } else {
            System.out.println("She doesn't fit me");
        }
    }
}
