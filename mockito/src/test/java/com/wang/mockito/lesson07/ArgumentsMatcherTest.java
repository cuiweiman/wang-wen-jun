package com.wang.mockito.lesson07;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description: Arguments Matchers
 * @author: wei·man cui
 * @date: 2020/8/13 16:56
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgumentsMatcherTest {

    @Test
    public void basicTest() {
        List<Integer> list = Mockito.mock(ArrayList.class);
        Mockito.when(list.get(0)).thenReturn(100);
        assertThat(list.get(0), equalTo(100));
        assertThat(list.get(1), CoreMatchers.nullValue());
    }

    /* isA, any 的不同 */
    @Test
    public void testComplex() {
        Foo foo = Mockito.mock(Foo.class);
        // Mockito.when(foo.function(Mockito.isA(Parent.class))).thenReturn(100);
        Mockito.when(foo.function(Mockito.isA(Child1.class))).thenReturn(100);
        int result = foo.function(new Child1());
        assertThat(result, equalTo(100));

        result = foo.function(new Child2());
        assertThat(result, equalTo(0));

        Mockito.reset(foo);

        Mockito.when(foo.function(Mockito.any(Child1.class))).thenReturn(100);
        result = foo.function(new Child2());
        assertThat(result, equalTo(100));
    }

    static class Foo {
        int function(Parent p) {
            return p.work();
        }
    }

    interface Parent {
        int work();
    }

    class Child1 implements Parent {
        @Override
        public int work() {
            throw new RuntimeException();
        }
    }

    class Child2 implements Parent {
        @Override
        public int work() {
            throw new RuntimeException();
        }
    }
}
