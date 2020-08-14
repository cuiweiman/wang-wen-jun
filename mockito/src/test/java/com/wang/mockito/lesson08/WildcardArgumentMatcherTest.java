package com.wang.mockito.lesson08;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;


/**
 * @description: Wildcard Argument
 * @author: wei·man cui
 * @date: 2020/8/14 10:22
 */
@RunWith(MockitoJUnitRunner.class)
public class WildcardArgumentMatcherTest {

    @Mock
    private SimpleService simpleService;

    @After
    public void destory() {
        Mockito.reset(simpleService);
    }

    @Test
    public void wildCardMethod1() {
        Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class)))
                .thenReturn(100);
        int result = simpleService.method1(1, "Sun", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(100));
    }

    /**
     * 对 Mockito.when() 参数进行特殊化 处理。入参不同，返回结果不同。
     */
    @Test
    public void wildCardMethodWithSpecial() {
        Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class))).thenReturn(-1);
        Mockito.when(simpleService.method1(anyInt(), eq("Jack"), anyCollection(), isA(Serializable.class))).thenReturn(100);
        Mockito.when(simpleService.method1(anyInt(), eq("Rose"), anyCollection(), isA(Serializable.class))).thenReturn(200);
        // 注意顺序，统配的 在最前，特殊的 在后面
        // Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class))).thenReturn(-1);
        int result = simpleService.method1(1, "Jack", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(100));
        result = simpleService.method1(1, "Rose", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(200));
        result = simpleService.method1(1, "Sun", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(-1));
    }

    @Test
    public void wildCardMethod2() {
        Mockito.doNothing().when(simpleService).method2(anyInt(), anyString(), anyCollection(), isA(Serializable.class));

        simpleService.method2(2, "Sun", Collections.emptyList(), "Mockito");
        Mockito.verify(simpleService, Mockito.times(1))
                .method2(anyInt(), eq("Sun"), anyCollection(), isA(Serializable.class));
    }

}
