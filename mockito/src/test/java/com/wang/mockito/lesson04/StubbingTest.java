package com.wang.mockito.lesson04;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;

/**
 * @description: Stubbing语法
 * @author: wei·man cui
 * @date: 2020/8/13 14:16
 */
@RunWith(MockitoJUnitRunner.class)
public class StubbingTest {

    private List<String> list;

    @Before
    public void init() {
        this.list = Mockito.mock(ArrayList.class);
    }

    @After
    public void destory() {
        Mockito.reset(this.list);
    }

    /**
     * 使用 Stubbing 语法进行模拟
     */
    @Test
    public void howToUseStubbing() {
        Mockito.when(list.get(0)).thenReturn("first");
        assertThat(list.get(0), equalTo("first"));

        Mockito.when(list.get(anyInt())).thenThrow(new RuntimeException());
        try {
            list.get(0);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    /**
     * Stubbing 测试 模拟 void 无返回值 方法
     */
    @Test
    public void howToStubbingVoidMethod() {
        Mockito.doNothing().when(list).clear();
        list.clear();
        Mockito.verify(list, Mockito.times(1)).clear();

        Mockito.doThrow(RuntimeException.class).when(list).clear();
        try {
            list.clear();
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    @Test
    public void stubbingDoReturn() {
        // 等价
        Mockito.when(list.get(0)).thenReturn("first");
        Mockito.doReturn("second").when(list).get(1);

        assertThat(list.get(0), equalTo("first"));
        assertThat(list.get(1), equalTo("second"));
    }

    /**
     * 迭代.调用次序不同，返回内容不同
     */
    @Test
    public void iterateStubbing() {
        Mockito.when(list.size()).thenReturn(1, 2, 3, 4);
        // Mockito.when(list.size()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);
        assertThat(list.size(), equalTo(1));
        assertThat(list.size(), equalTo(2));
        assertThat(list.size(), equalTo(3));
        assertThat(list.size(), equalTo(4));
    }

    @Test
    public void stubbingWithAnswer() {
        Mockito.when(list.get(anyInt())).thenAnswer(invocationOnMock -> {
            Integer index = invocationOnMock.getArgumentAt(0, Integer.class);
            return String.valueOf(index * 10);
        });
        assertThat(list.get(0), equalTo("0"));
        assertThat(list.get(99), equalTo("990"));
    }

    @Test
    public void stubbingWithRealCall() {
        StubbingService stubbingService = Mockito.mock(StubbingService.class);
        Mockito.when(stubbingService.getS()).thenReturn("String");
        Mockito.when(stubbingService.getI()).thenCallRealMethod();

        assertThat(stubbingService.getS(), equalTo("String"));
        assertThat(stubbingService.getI(), equalTo(10));
    }

}
